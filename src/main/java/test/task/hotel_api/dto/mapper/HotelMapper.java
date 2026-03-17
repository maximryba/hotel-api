package test.task.hotel_api.dto.mapper;

import org.springframework.stereotype.Component;
import test.task.hotel_api.dto.request.HotelCreateRequest;
import test.task.hotel_api.entity.Address;
import test.task.hotel_api.entity.ArrivalTime;
import test.task.hotel_api.entity.Contacts;
import test.task.hotel_api.entity.Hotel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class HotelMapper {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Hotel toEntity(HotelCreateRequest request) {
        if (request == null) {
            return null;
        }

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setBrand(request.getBrand());

        // Map Address
        if (request.getAddress() != null) {
            Address address = new Address();
            address.setHouseNumber(request.getAddress().getHouseNumber());
            address.setStreet(request.getAddress().getStreet());
            address.setCity(request.getAddress().getCity());
            address.setCountry(request.getAddress().getCountry());
            address.setPostCode(request.getAddress().getPostCode());
            hotel.setAddress(address);
        }

        // Map Contacts
        if (request.getContacts() != null) {
            Contacts contacts = new Contacts();
            contacts.setPhone(request.getContacts().getPhone());
            contacts.setEmail(request.getContacts().getEmail());
            hotel.setContacts(contacts);
        }

        // Map ArrivalTime
        if (request.getArrivalTime() != null) {
            ArrivalTime arrivalTime = new ArrivalTime();
            if (request.getArrivalTime().getCheckIn() != null) {
                arrivalTime.setCheckIn(LocalTime.parse(request.getArrivalTime().getCheckIn(), TIME_FORMATTER));
            }
            if (request.getArrivalTime().getCheckOut() != null) {
                arrivalTime.setCheckOut(LocalTime.parse(request.getArrivalTime().getCheckOut(), TIME_FORMATTER));
            }
            hotel.setArrivalTime(arrivalTime);
        }

        return hotel;
    }

    public void updateEntity(Hotel hotel, HotelCreateRequest request) {
        if (request == null) {
            return;
        }

        if (request.getName() != null) {
            hotel.setName(request.getName());
        }

        if (request.getDescription() != null) {
            hotel.setDescription(request.getDescription());
        }

        if (request.getBrand() != null) {
            hotel.setBrand(request.getBrand());
        }

        // Update Address
        if (request.getAddress() != null && hotel.getAddress() != null) {
            Address address = hotel.getAddress();
            if (request.getAddress().getHouseNumber() != null) {
                address.setHouseNumber(request.getAddress().getHouseNumber());
            }
            if (request.getAddress().getStreet() != null) {
                address.setStreet(request.getAddress().getStreet());
            }
            if (request.getAddress().getCity() != null) {
                address.setCity(request.getAddress().getCity());
            }
            if (request.getAddress().getCountry() != null) {
                address.setCountry(request.getAddress().getCountry());
            }
            if (request.getAddress().getPostCode() != null) {
                address.setPostCode(request.getAddress().getPostCode());
            }
        }

        // Update Contacts
        if (request.getContacts() != null && hotel.getContacts() != null) {
            Contacts contacts = hotel.getContacts();
            if (request.getContacts().getPhone() != null) {
                contacts.setPhone(request.getContacts().getPhone());
            }
            if (request.getContacts().getEmail() != null) {
                contacts.setEmail(request.getContacts().getEmail());
            }
        }

        // Update ArrivalTime
        if (request.getArrivalTime() != null && hotel.getArrivalTime() != null) {
            ArrivalTime arrivalTime = hotel.getArrivalTime();
            if (request.getArrivalTime().getCheckIn() != null) {
                arrivalTime.setCheckIn(LocalTime.parse(request.getArrivalTime().getCheckIn(), TIME_FORMATTER));
            }
            if (request.getArrivalTime().getCheckOut() != null) {
                arrivalTime.setCheckOut(LocalTime.parse(request.getArrivalTime().getCheckOut(), TIME_FORMATTER));
            }
        }
    }
}
