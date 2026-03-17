package test.task.hotel_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.task.hotel_api.entity.Amenity;
import test.task.hotel_api.entity.Hotel;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDetailResponse {

    private Long id;
    private String name;
    private String description;
    private String brand;
    private AddressResponse address;
    private ContactsResponse contacts;
    private ArrivalTimeResponse arrivalTime;
    private List<String> amenities;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressResponse {

        @JsonProperty("houseNumber")
        private String houseNumber;

        private String street;
        private String city;
        private String country;

        @JsonProperty("postCode")
        private String postCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactsResponse {
        private String phone;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArrivalTimeResponse {

        @JsonProperty("checkIn")
        private String checkIn;

        @JsonProperty("checkOut")
        private String checkOut;
    }

    public static HotelDetailResponse fromEntity(Hotel hotel) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return HotelDetailResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .brand(hotel.getBrand())
                .address(AddressResponse.builder()
                        .houseNumber(hotel.getAddress().getHouseNumber())
                        .street(hotel.getAddress().getStreet())
                        .city(hotel.getAddress().getCity())
                        .country(hotel.getAddress().getCountry())
                        .postCode(hotel.getAddress().getPostCode())
                        .build())
                .contacts(ContactsResponse.builder()
                        .phone(hotel.getContacts().getPhone())
                        .email(hotel.getContacts().getEmail())
                        .build())
                .arrivalTime(ArrivalTimeResponse.builder()
                        .checkIn(hotel.getArrivalTime().getCheckIn().format(timeFormatter))
                        .checkOut(hotel.getArrivalTime().getCheckOut().format(timeFormatter))
                        .build())
                .amenities(hotel.getAmenities().stream()
                        .map(Amenity::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
