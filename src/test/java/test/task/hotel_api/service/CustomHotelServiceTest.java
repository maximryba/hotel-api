package test.task.hotel_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.task.hotel_api.dto.mapper.HotelMapper;
import test.task.hotel_api.dto.request.HotelCreateRequest;
import test.task.hotel_api.dto.response.HotelCreateResponse;
import test.task.hotel_api.dto.response.HotelDetailResponse;
import test.task.hotel_api.dto.response.HotelShortResponse;
import test.task.hotel_api.entity.*;
import test.task.hotel_api.exception.HotelNotFoundException;
import test.task.hotel_api.repository.HotelRepository;

import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomHotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private CustomHotelService hotelService;

    @Captor
    private ArgumentCaptor<Hotel> hotelCaptor;

    private Hotel testHotel;
    private Address testAddress;
    private Contacts testContacts;
    private ArrivalTime testArrivalTime;
    private Set<Amenity> testAmenities;

    @BeforeEach
    void setUp() {
        // Setup Address
        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setHouseNumber("9");
        testAddress.setStreet("Pobediteley Avenue");
        testAddress.setCity("Minsk");
        testAddress.setCountry("Belarus");
        testAddress.setPostCode("220004");

        // Setup Contacts
        testContacts = new Contacts();
        testContacts.setId(1L);
        testContacts.setPhone("+375 17 309-80-00");
        testContacts.setEmail("test@hotel.com");

        // Setup ArrivalTime
        testArrivalTime = new ArrivalTime();
        testArrivalTime.setId(1L);
        testArrivalTime.setCheckIn(LocalTime.of(14, 0));
        testArrivalTime.setCheckOut(LocalTime.of(12, 0));

        // Setup Amenities
        testAmenities = new HashSet<>();
        Amenity wifi = new Amenity();
        wifi.setId(1L);
        wifi.setName("Free WiFi");
        testAmenities.add(wifi);

        Amenity parking = new Amenity();
        parking.setId(2L);
        parking.setName("Free parking");
        testAmenities.add(parking);

        // Setup Hotel
        testHotel = new Hotel();
        testHotel.setId(1L);
        testHotel.setName("DoubleTree by Hilton Minsk");
        testHotel.setDescription("Luxurious hotel in Minsk");
        testHotel.setBrand("Hilton");
        testHotel.setAddress(testAddress);
        testHotel.setContacts(testContacts);
        testHotel.setArrivalTime(testArrivalTime);
        testHotel.setAmenities(testAmenities);
    }

    @Test
    @DisplayName("Should return all hotels with basic info")
    void getAllHotels_ShouldReturnListOfHotels() {
        // Arrange
        List<Hotel> hotels = Arrays.asList(testHotel, createAnotherHotel());
        when(hotelRepository.findAllWithBasicInfo()).thenReturn(hotels);

        // Act
        List<HotelShortResponse> result = hotelService.getAllHotels();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(hotelRepository, times(1)).findAllWithBasicInfo();
    }

    @Test
    @DisplayName("Should return empty list when no hotels exist")
    void getAllHotels_WhenNoHotels_ShouldReturnEmptyList() {
        // Arrange
        when(hotelRepository.findAllWithBasicInfo()).thenReturn(Collections.emptyList());

        // Act
        List<HotelShortResponse> result = hotelService.getAllHotels();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(hotelRepository, times(1)).findAllWithBasicInfo();
    }

    @Test
    @DisplayName("Should return hotel details by id when hotel exists")
    void getHotelById_WhenHotelExists_ShouldReturnHotelDetails() {
        // Arrange
        Long hotelId = 1L;
        when(hotelRepository.findByIdWithDetails(hotelId)).thenReturn(Optional.of(testHotel));

        // Act
        HotelDetailResponse result = hotelService.getHotelById(hotelId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testHotel.getId());
        assertThat(result.getName()).isEqualTo(testHotel.getName());
        assertThat(result.getBrand()).isEqualTo(testHotel.getBrand());
        assertThat(result.getAddress().getCity()).isEqualTo(testAddress.getCity());
        assertThat(result.getContacts().getPhone()).isEqualTo(testContacts.getPhone());
        assertThat(result.getAmenities()).hasSize(2);

        verify(hotelRepository, times(1)).findByIdWithDetails(hotelId);
    }

    @Test
    @DisplayName("Should throw exception when hotel not found by id")
    void getHotelById_WhenHotelNotFound_ShouldThrowException() {
        // Arrange
        Long hotelId = 999L;
        when(hotelRepository.findByIdWithDetails(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> hotelService.getHotelById(hotelId))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining("Hotel not found with id: " + hotelId);

        verify(hotelRepository, times(1)).findByIdWithDetails(hotelId);
    }

    @Test
    @DisplayName("Should search hotels by criteria")
    void searchHotels_WithValidCriteria_ShouldReturnMatchingHotels() {
        // Arrange
        String name = "Hilton";
        String brand = "Hilton";
        String city = "Minsk";
        String country = "Belarus";
        String amenities = "WiFi,parking";

        List<Hotel> hotels = Collections.singletonList(testHotel);
        when(hotelRepository.searchHotels(name, brand, city, country, amenities))
                .thenReturn(hotels);

        // Act
        List<HotelShortResponse> result = hotelService.searchHotels(name, brand, city, country, amenities);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(hotelRepository, times(1))
                .searchHotels(name, brand, city, country, amenities);
    }

    @Test
    @DisplayName("Should add new amenities to existing hotel")
    void addAmenitiesToHotel_WithNewAmenities_ShouldAddSuccessfully() {
        // Arrange
        Long hotelId = 1L;
        List<String> newAmenities = Arrays.asList("Pool", "Spa");

        when(hotelRepository.findByIdWithDetails(hotelId)).thenReturn(Optional.of(testHotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(testHotel);

        int initialAmenitiesCount = testHotel.getAmenities().size();

        // Act
        hotelService.addAmenitiesToHotel(hotelId, newAmenities);

        // Assert
        verify(hotelRepository, times(1)).findByIdWithDetails(hotelId);
        verify(hotelRepository, times(1)).save(testHotel);

        assertThat(testHotel.getAmenities()).hasSize(initialAmenitiesCount + 2);

        Set<String> amenityNames = testHotel.getAmenities().stream()
                .map(Amenity::getName)
                .collect(java.util.stream.Collectors.toSet());
        assertThat(amenityNames).contains("Pool", "Spa");
    }

    @Test
    @DisplayName("Should not add duplicate amenities")
    void addAmenitiesToHotel_WithDuplicateAmenities_ShouldSkipDuplicates() {
        // Arrange
        Long hotelId = 1L;
        List<String> newAmenities = Arrays.asList("Free WiFi", "Spa"); // Free WiFi already exists

        when(hotelRepository.findByIdWithDetails(hotelId)).thenReturn(Optional.of(testHotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(testHotel);

        int initialAmenitiesCount = testHotel.getAmenities().size();

        // Act
        hotelService.addAmenitiesToHotel(hotelId, newAmenities);

        // Assert
        assertThat(testHotel.getAmenities()).hasSize(initialAmenitiesCount + 1); // Only Spa added

        Set<String> amenityNames = testHotel.getAmenities().stream()
                .map(Amenity::getName)
                .collect(java.util.stream.Collectors.toSet());
        assertThat(amenityNames).contains("Free WiFi", "Spa");
    }

    @Test
    @DisplayName("Should throw exception when adding amenities to non-existent hotel")
    void addAmenitiesToHotel_WhenHotelNotFound_ShouldThrowException() {
        // Arrange
        Long hotelId = 999L;
        List<String> newAmenities = Arrays.asList("Pool", "Spa");

        when(hotelRepository.findByIdWithDetails(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> hotelService.addAmenitiesToHotel(hotelId, newAmenities))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining("Hotel not found with id: " + hotelId);

        verify(hotelRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should generate histogram by brand")
    void getHistogram_ByBrand_ShouldReturnBrandCounts() {
        // Arrange
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"Hilton", 5L},
                new Object[]{"Marriott", 3L},
                new Object[]{"Sheraton", 2L}
        );

        when(hotelRepository.countByBrand()).thenReturn(mockResults);

        // Act
        Map<String, Long> result = hotelService.getHistogram("brand");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("Hilton", 5L);
        assertThat(result).containsEntry("Marriott", 3L);
        assertThat(result).containsEntry("Sheraton", 2L);

        verify(hotelRepository, times(1)).countByBrand();
        verify(hotelRepository, never()).countByCity();
        verify(hotelRepository, never()).countByCountry();
        verify(hotelRepository, never()).countByAmenities();
    }

    @Test
    @DisplayName("Should generate histogram by city")
    void getHistogram_ByCity_ShouldReturnCityCounts() {
        // Arrange
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"Minsk", 10L},
                new Object[]{"Moscow", 7L},
                new Object[]{"London", 5L}
        );

        when(hotelRepository.countByCity()).thenReturn(mockResults);

        // Act
        Map<String, Long> result = hotelService.getHistogram("city");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("Minsk", 10L);
        assertThat(result).containsEntry("Moscow", 7L);
        assertThat(result).containsEntry("London", 5L);

        verify(hotelRepository, times(1)).countByCity();
    }

    @Test
    @DisplayName("Should generate histogram by country")
    void getHistogram_ByCountry_ShouldReturnCountryCounts() {
        // Arrange
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"Belarus", 15L},
                new Object[]{"Russia", 12L},
                new Object[]{"USA", 8L}
        );

        when(hotelRepository.countByCountry()).thenReturn(mockResults);

        // Act
        Map<String, Long> result = hotelService.getHistogram("country");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("Belarus", 15L);
        assertThat(result).containsEntry("Russia", 12L);
        assertThat(result).containsEntry("USA", 8L);

        verify(hotelRepository, times(1)).countByCountry();
    }

    @Test
    @DisplayName("Should generate histogram by amenities")
    void getHistogram_ByAmenities_ShouldReturnAmenitiesCounts() {
        // Arrange
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"Free WiFi", 25L},
                new Object[]{"Parking", 20L},
                new Object[]{"Pool", 15L}
        );

        when(hotelRepository.countByAmenities()).thenReturn(mockResults);

        // Act
        Map<String, Long> result = hotelService.getHistogram("amenities");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsEntry("Free WiFi", 25L);
        assertThat(result).containsEntry("Parking", 20L);
        assertThat(result).containsEntry("Pool", 15L);

        verify(hotelRepository, times(1)).countByAmenities();
    }

    @ParameterizedTest
    @ValueSource(strings = {"brand", "city", "country", "amenities"})
    @DisplayName("Should handle null values in histogram results")
    void getHistogram_WithNullValues_ShouldHandleGracefully(String param) {
        // Arrange
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{null, 5L},
                new Object[]{"Valid", null},
                new Object[]{null, null}
        );

        switch (param) {
            case "brand":
                when(hotelRepository.countByBrand()).thenReturn(mockResults);
                break;
            case "city":
                when(hotelRepository.countByCity()).thenReturn(mockResults);
                break;
            case "country":
                when(hotelRepository.countByCountry()).thenReturn(mockResults);
                break;
            case "amenities":
                when(hotelRepository.countByAmenities()).thenReturn(mockResults);
                break;
        }

        // Act
        Map<String, Long> result = hotelService.getHistogram(param);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("Not specified");
        assertThat(result).containsValue(0L);
    }

    @Test
    @DisplayName("Should throw exception for unsupported histogram parameter")
    void getHistogram_WithUnsupportedParam_ShouldThrowException() {
        // Act & Assert
        assertThatThrownBy(() -> hotelService.getHistogram("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported histogram parameter: invalid");

        verify(hotelRepository, never()).countByBrand();
        verify(hotelRepository, never()).countByCity();
        verify(hotelRepository, never()).countByCountry();
        verify(hotelRepository, never()).countByAmenities();
    }

    @Test
    @DisplayName("Should handle case-insensitive histogram parameters")
    void getHistogram_WithCaseInsensitiveParams_ShouldWork() {
        // Arrange
        List<Object[]> mockResults = Collections.singletonList(new Object[]{"Hilton", 5L});
        when(hotelRepository.countByBrand()).thenReturn(mockResults);

        // Act
        Map<String, Long> result = hotelService.getHistogram("BRAND");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(hotelRepository, times(1)).countByBrand();
    }

    // Helper methods
    private HotelCreateRequest createValidHotelCreateRequest() {
        HotelCreateRequest request = new HotelCreateRequest();
        request.setName("DoubleTree by Hilton Minsk");
        request.setDescription("Luxurious hotel in Minsk");
        request.setBrand("Hilton");

        return request;
    }

    private Hotel createAnotherHotel() {
        Hotel hotel = new Hotel();
        hotel.setId(2L);
        hotel.setName("Marriott Moscow");
        hotel.setBrand("Marriott");

        Address address = new Address();
        address.setCity("Moscow");
        address.setCountry("Russia");
        hotel.setAddress(address);

        return hotel;
    }
}
