package test.task.hotel_api.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.task.hotel_api.entity.Hotel;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<@NonNull Hotel, @NonNull Long> {

    @Query("SELECT DISTINCT h FROM Hotel h " +
            "LEFT JOIN FETCH h.address " +
            "LEFT JOIN FETCH h.contacts " +
            "LEFT JOIN FETCH h.amenities a " +
            "WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:brand IS NULL OR LOWER(h.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) " +
            "AND (:city IS NULL OR LOWER(h.address.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:country IS NULL OR LOWER(h.address.country) LIKE LOWER(CONCAT('%', :country, '%'))) " +
            "AND (:amenity IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :amenity, '%')))")
    List<Hotel> searchHotels(@Param("name") String name,
                             @Param("brand") String brand,
                             @Param("city") String city,
                             @Param("country") String country,
                             @Param("amenity") String amenity);


    @Query("SELECT h FROM Hotel h " +
            "LEFT JOIN FETCH h.address " +
            "LEFT JOIN FETCH h.contacts " +
            "LEFT JOIN FETCH h.arrivalTime " +
            "LEFT JOIN FETCH h.amenities " +
            "WHERE h.id = :id")
    java.util.Optional<Hotel> findByIdWithDetails(@Param("id") Long id);


    @Query("SELECT DISTINCT h FROM Hotel h " +
            "LEFT JOIN FETCH h.address " +
            "LEFT JOIN FETCH h.contacts")
    List<Hotel> findAllWithBasicInfo();


    @Query("SELECT COUNT(h) > 0 FROM Hotel h " +
            "WHERE LOWER(h.name) = LOWER(:name) " +
            "AND LOWER(h.address.street) = LOWER(:street) " +
            "AND LOWER(h.address.city) = LOWER(:city) " +
            "AND LOWER(h.address.country) = LOWER(:country)")
    boolean existsByNameAndAddress(@Param("name") String name,
                                   @Param("street") String street,
                                   @Param("city") String city,
                                   @Param("country") String country);

    // Histogram Queries

    @Query("SELECT h.address.country, COUNT(h) c FROM Hotel h GROUP BY h.address.country ORDER BY c DESC")
    List<Object[]> countByCountry();

    @Query("SELECT h.address.city, COUNT(h) c FROM Hotel h GROUP BY h.address.city ORDER BY c DESC")
    List<Object[]> countByCity();

    @Query("SELECT h.brand, COUNT(h) c FROM Hotel h WHERE h.brand IS NOT NULL GROUP BY h.brand ORDER BY c DESC")
    List<Object[]> countByBrand();

    @Query("SELECT a.name, COUNT(h) c FROM Hotel h JOIN h.amenities a GROUP BY a.name ORDER BY c DESC")
    List<Object[]> countByAmenities();
}
