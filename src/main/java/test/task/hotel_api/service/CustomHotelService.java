package test.task.hotel_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.task.hotel_api.dto.mapper.HotelMapper;
import test.task.hotel_api.dto.request.HotelCreateRequest;
import test.task.hotel_api.dto.response.HotelCreateResponse;
import test.task.hotel_api.dto.response.HotelDetailResponse;
import test.task.hotel_api.dto.response.HotelShortResponse;
import test.task.hotel_api.entity.*;
import test.task.hotel_api.exception.HotelNotFoundException;
import test.task.hotel_api.repository.HotelRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomHotelService implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<HotelShortResponse> getAllHotels() {
        log.debug("Fetching all hotels with basic info");
        return hotelRepository.findAllWithBasicInfo().stream()
                .map(HotelShortResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public HotelDetailResponse getHotelById(Long id) {
        log.debug("Fetching hotel details for id: {}", id);
        Hotel hotel = hotelRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));
        return HotelDetailResponse.fromEntity(hotel);
    }

    @Override
    public List<HotelShortResponse> searchHotels(String name, String brand, String city,
                                                 String country, String amenities) {
        log.debug("Searching hotels with criteria - name: {}, brand: {}, city: {}, country: {}, amenities: {}",
                name, brand, city, country, amenities);

        List<Hotel> hotels = hotelRepository.searchHotels(name, brand, city, country, amenities);

        return hotels.stream()
                .map(HotelShortResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotelCreateResponse createHotel(HotelCreateRequest request) {
        log.debug("Creating new hotel: {}", request.getName());

        Address address = new Address();
        address.setHouseNumber(request.getAddress().getHouseNumber());
        address.setStreet(request.getAddress().getStreet());
        address.setCity(request.getAddress().getCity());
        address.setCountry(request.getAddress().getCountry());
        address.setPostCode(request.getAddress().getPostCode());

        Contacts contacts = new Contacts();
        contacts.setPhone(request.getContacts().getPhone());
        contacts.setEmail(request.getContacts().getEmail());

        ArrivalTime arrivalTime = new ArrivalTime();
        if (request.getArrivalTime().getCheckIn() != null) {
            arrivalTime.setCheckIn(LocalTime.parse(request.getArrivalTime().getCheckIn(), TIME_FORMATTER));
        }
        if (request.getArrivalTime().getCheckOut() != null) {
            arrivalTime.setCheckOut(LocalTime.parse(request.getArrivalTime().getCheckOut(), TIME_FORMATTER));
        }

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setBrand(request.getBrand());

        hotel.setAddress(address);
        hotel.setContacts(contacts);
        hotel.setArrivalTime(arrivalTime);

        address.setHotel(hotel);
        contacts.setHotel(hotel);
        arrivalTime.setHotel(hotel);

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully with id: {}", savedHotel.getId());

        return HotelCreateResponse.fromEntity(savedHotel);
    }

    @Override
    @Transactional
    public void addAmenitiesToHotel(Long hotelId, List<String> amenityNames) {
        log.debug("Adding amenities to hotel id: {}, amenities: {}", hotelId, amenityNames);

        Hotel hotel = hotelRepository.findByIdWithDetails(hotelId)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + hotelId));

        Set<Amenity> currentAmenities = hotel.getAmenities();
        Set<String> currentAmenityNames = currentAmenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());

        for (String amenityName : amenityNames) {
            if (!currentAmenityNames.contains(amenityName)) {
                Amenity amenity = new Amenity();
                amenity.setName(amenityName);

                amenity.getHotels().add(hotel);
                currentAmenities.add(amenity);

                log.debug("Added new amenity: {} to hotel: {}", amenityName, hotelId);
            }
        }

        hotelRepository.save(hotel);
        log.info("Amenities added successfully to hotel id: {}", hotelId);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        log.debug("Generating histogram for param: {}", param);

        List<Object[]> results;

        switch (param.toLowerCase()) {
            case "brand":
                results = hotelRepository.countByBrand();
                break;
            case "city":
                results = hotelRepository.countByCity();
                break;
            case "country":
                results = hotelRepository.countByCountry();
                break;
            case "amenities":
                results = hotelRepository.countByAmenities();
                break;
            default:
                throw new IllegalArgumentException("Unsupported histogram parameter: " + param +
                        ". Supported values: brand, city, country, amenities");
        }

        return convertToHistogramMap(results);
    }

    /**
     * Вспомогательный метод для конвертации результатов запроса в Map
     */
    private Map<String, Long> convertToHistogramMap(List<Object[]> results) {
        Map<String, Long> histogram = new LinkedHashMap<>();

        for (Object[] result : results) {
            String key = result[0] != null ? result[0].toString() : "Not specified";
            Long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;
            histogram.put(key, count);
        }

        return histogram;
    }
}
