package test.task.hotel_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.task.hotel_api.entity.Hotel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelShortResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;

    public static HotelShortResponse fromEntity(Hotel hotel) {
        return HotelShortResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(hotel.getFullAddress())
                .phone(hotel.getPhone())
                .build();
    }
}
