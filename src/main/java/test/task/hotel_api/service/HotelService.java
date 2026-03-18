package test.task.hotel_api.service;

import test.task.hotel_api.dto.request.HotelCreateRequest;
import test.task.hotel_api.dto.response.HotelCreateResponse;
import test.task.hotel_api.dto.response.HotelDetailResponse;
import test.task.hotel_api.dto.response.HotelShortResponse;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelShortResponse> getAllHotels();

    HotelDetailResponse getHotelById(Long id);

    List<HotelShortResponse> searchHotels(String name, String brand, String city,
                                          String country, String amenities);

    HotelCreateResponse createHotel(HotelCreateRequest request);

    void addAmenitiesToHotel(Long hotelId, List<String> amenityNames);

    Map<String, Long> getHistogram(String param);
}