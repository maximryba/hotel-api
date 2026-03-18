package test.task.hotel_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.task.hotel_api.dto.request.HotelCreateRequest;
import test.task.hotel_api.dto.response.ErrorResponse;
import test.task.hotel_api.dto.response.HotelCreateResponse;
import test.task.hotel_api.dto.response.HotelDetailResponse;
import test.task.hotel_api.dto.response.HotelShortResponse;
import test.task.hotel_api.service.HotelService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Hotel management endpoints")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(
            summary = "Get all hotels",
            description = "Retrieves a list of all hotels with brief information (id, name, description, address, phone)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = HotelShortResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<@NonNull List<HotelShortResponse>> getAllHotels() {
        log.info("REST request to get all hotels");
        List<HotelShortResponse> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/hotels/{id}")
    @Operation(
            summary = "Get hotel by ID",
            description = "Retrieves detailed information about a specific hotel by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotel",
                    content = @Content(schema = @Schema(implementation = HotelDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<@NonNull HotelDetailResponse> getHotelById(
            @Parameter(description = "ID of the hotel to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        log.info("REST request to get hotel by id: {}", id);
        HotelDetailResponse hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search hotels",
            description = "Search hotels by various criteria (name, brand, city, country, amenities). All parameters are optional."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = HotelShortResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<@NonNull List<HotelShortResponse>> searchHotels(
            @Parameter(description = "Hotel name (partial match)", example = "Hilton")
            @RequestParam(required = false) String name,

            @Parameter(description = "Hotel brand (partial match)", example = "Hilton")
            @RequestParam(required = false) String brand,

            @Parameter(description = "City name (partial match)", example = "Minsk")
            @RequestParam(required = false) String city,

            @Parameter(description = "Country name (partial match)", example = "Belarus")
            @RequestParam(required = false) String country,

            @Parameter(description = "Amenity name (partial match)", example = "WiFi")
            @RequestParam(required = false) String amenities) {

        log.info("REST request to search hotels with criteria: name={}, brand={}, city={}, country={}, amenities={}",
                name, brand, city, country, amenities);

        List<HotelShortResponse> hotels = hotelService.searchHotels(name, brand, city, country, amenities);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping("/hotels")
    @Operation(
            summary = "Create a new hotel",
            description = "Creates a new hotel with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel successfully created",
                    content = @Content(schema = @Schema(implementation = HotelCreateResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<@NonNull HotelCreateResponse> createHotel(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Hotel data to create",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = HotelCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "Hotel creation example",
                                    value = "{\n" +
                                            "  \"name\": \"DoubleTree by Hilton Minsk\",\n" +
                                            "  \"description\": \"The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...\",\n" +
                                            "  \"brand\": \"Hilton\",\n" +
                                            "  \"address\": {\n" +
                                            "    \"houseNumber\": \"9\",\n" +
                                            "    \"street\": \"Pobediteley Avenue\",\n" +
                                            "    \"city\": \"Minsk\",\n" +
                                            "    \"country\": \"Belarus\",\n" +
                                            "    \"postCode\": \"220004\"\n" +
                                            "  },\n" +
                                            "  \"contacts\": {\n" +
                                            "    \"phone\": \"+375 17 309-80-00\",\n" +
                                            "    \"email\": \"doubletreeminsk.info@hilton.com\"\n" +
                                            "  },\n" +
                                            "  \"arrivalTime\": {\n" +
                                            "    \"checkIn\": \"14:00\",\n" +
                                            "    \"checkOut\": \"12:00\"\n" +
                                            "  }\n" +
                                            "}"
                            )
                    )
            )
            @Valid @RequestBody HotelCreateRequest request) {

        log.info("REST request to create new hotel: {}", request.getName());
        HotelCreateResponse response = hotelService.createHotel(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(
            summary = "Add amenities to hotel",
            description = "Adds a list of amenities to an existing hotel"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amenities successfully added"),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> addAmenitiesToHotel(
            @Parameter(description = "ID of the hotel", required = true, example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of amenity names to add",
                    required = true,
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = String.class)),
                            examples = @ExampleObject(
                                    name = "Amenities example",
                                    value = "[\n" +
                                            "  \"Free parking\",\n" +
                                            "  \"Free WiFi\",\n" +
                                            "  \"Non-smoking rooms\",\n" +
                                            "  \"Fitness center\"\n" +
                                            "]"
                            )
                    )
            )
            @Valid @RequestBody List<String> amenities) {

        log.info("REST request to add amenities to hotel id: {}, amenities: {}", id, amenities);
        hotelService.addAmenitiesToHotel(id, amenities);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histogram/{param}")
    @Operation(
            summary = "Get histogram",
            description = "Returns a histogram (count of hotels grouped by the specified parameter)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated histogram",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter. Supported: brand, city, country, amenities",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Map<String, Long>> getHistogram(
            @Parameter(description = "Parameter to group by", required = true,
                    example = "city", schema = @Schema(allowableValues = {"brand", "city", "country", "amenities"}))
            @PathVariable String param) {

        log.info("REST request to get histogram for param: {}", param);
        Map<String, Long> histogram = hotelService.getHistogram(param);
        return ResponseEntity.ok(histogram);
    }
}
