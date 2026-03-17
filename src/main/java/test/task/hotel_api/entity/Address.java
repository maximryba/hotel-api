package test.task.hotel_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_seq")
    @SequenceGenerator(name = "addresses_seq", sequenceName = "addresses_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(name = "house_number", nullable = false, length = 20)
    private String houseNumber;

    @NotBlank
    @Column(name = "street", nullable = false, length = 255)
    private String street;

    @NotBlank
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "post_code", length = 20)
    private String postCode;

    @OneToOne(mappedBy = "address")
    private Hotel hotel;

    public String getFullAddress() {
        return String.format("%s %s, %s, %s, %s",
                houseNumber, street, city, postCode, country);
    }
}
