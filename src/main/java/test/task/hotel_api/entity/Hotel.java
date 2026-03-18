package test.task.hotel_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "CLOB")
    private String description;

    @Column(name = "brand", length = 100)
    private String brand;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "contacts_id", referencedColumnName = "id", nullable = false)
    private Contacts contacts;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "arrival_time_id", referencedColumnName = "id", nullable = false)
    private ArrivalTime arrivalTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_amenities",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    public void addAmenity(Amenity amenity) {
        this.amenities.add(amenity);
        amenity.getHotels().add(this);
    }

    public void removeAmenity(Amenity amenity) {
        this.amenities.remove(amenity);
        amenity.getHotels().remove(this);
    }

    public void addAllAmenities(Set<Amenity> amenities) {
        for (Amenity amenity : amenities) {
            addAmenity(amenity);
        }
    }

    // Business methods for DTO conversion
    public String getFullAddress() {
        return address != null ? address.getFullAddress() : null;
    }

    public String getPhone() {
        return contacts != null ? contacts.getPhone() : null;
    }

}
