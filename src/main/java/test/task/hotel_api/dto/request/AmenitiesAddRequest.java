package test.task.hotel_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AmenitiesAddRequest {

    @NotEmpty(message = "Amenities list cannot be empty")
    private List<@NotBlank(message = "Amenity name cannot be blank") String> amenities;
}
