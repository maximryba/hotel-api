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
public class HotelCreateResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;

    public static HotelCreateResponse fromEntity(Hotel hotel) {
        return HotelCreateResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(hotel.getFullAddress())
                .phone(hotel.getPhone())
                .build();
    }
}
