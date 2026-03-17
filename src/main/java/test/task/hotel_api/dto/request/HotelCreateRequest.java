package test.task.hotel_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class HotelCreateRequest {

    @NotBlank(message = "Hotel name is required")
    private String name;

    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Address is required")
    @Valid
    private AddressRequest address;

    @NotNull(message = "Contacts are required")
    @Valid
    private ContactsRequest contacts;

    @NotNull(message = "Arrival time is required")
    @Valid
    private ArrivalTimeRequest arrivalTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressRequest {

        @NotBlank(message = "House number is required")
        @JsonProperty("houseNumber")
        private String houseNumber;

        @NotBlank(message = "Street is required")
        private String street;

        @NotBlank(message = "City is required")
        private String city;

        @NotBlank(message = "Country is required")
        private String country;

        @JsonProperty("postCode")
        private String postCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactsRequest {

        @NotBlank(message = "Phone is required")
        private String phone;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArrivalTimeRequest {

        @NotBlank(message = "Check-in time is required")
        @JsonProperty("checkIn")
        private String checkIn;

        @JsonProperty("checkOut")
        private String checkOut; // optional
    }
}
