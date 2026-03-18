package test.task.hotel_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new hotel")
public class HotelCreateRequest {

    @Schema(description = "Hotel name", required = true, example = "DoubleTree by Hilton Minsk")
    @NotBlank(message = "Hotel name is required")
    private String name;

    @Schema(description = "Hotel description", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
    private String description;

    @Schema(description = "Hotel brand", required = true, example = "Hilton")
    @NotBlank(message = "Brand is required")
    private String brand;

    @Schema(description = "Hotel address", required = true)
    @NotNull(message = "Address is required")
    @Valid
    private AddressRequest address;

    @Schema(description = "Hotel contact information", required = true)
    @NotNull(message = "Contacts are required")
    @Valid
    private ContactsRequest contacts;

    @Schema(description = "Hotel check-in/check-out times", required = true)
    @NotNull(message = "Arrival time is required")
    @Valid
    private ArrivalTimeRequest arrivalTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Address information")
    public static class AddressRequest {

        @Schema(description = "House number", required = true, example = "9")
        @NotBlank(message = "House number is required")
        @JsonProperty("houseNumber")
        private String houseNumber;

        @Schema(description = "Street name", required = true, example = "Pobediteley Avenue")
        @NotBlank(message = "Street is required")
        private String street;

        @Schema(description = "City", required = true, example = "Minsk")
        @NotBlank(message = "City is required")
        private String city;

        @Schema(description = "Country", required = true, example = "Belarus")
        @NotBlank(message = "Country is required")
        private String country;

        @Schema(description = "Postal code", example = "220004")
        @JsonProperty("postCode")
        private String postCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Contact information")
    public static class ContactsRequest {

        @Schema(description = "Phone number", required = true, example = "+375 17 309-80-00")
        @NotBlank(message = "Phone is required")
        private String phone;

        @Schema(description = "Email address", required = true, example = "doubletreeminsk.info@hilton.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Check-in/Check-out times")
    public static class ArrivalTimeRequest {

        @Schema(description = "Check-in time (format: HH:mm)", required = true, example = "14:00")
        @NotBlank(message = "Check-in time is required")
        @JsonProperty("checkIn")
        private String checkIn;

        @Schema(description = "Check-out time (format: HH:mm)", example = "12:00")
        @JsonProperty("checkOut")
        private String checkOut;
    }
}
