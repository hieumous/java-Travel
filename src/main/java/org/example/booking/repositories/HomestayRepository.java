package org.example.booking.repositories;

import org.example.booking.models.Homestay;
import org.example.booking.models.User;
import org.example.booking.enums.HomestayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomestayRepository extends JpaRepository<Homestay, Long> {

    List<Homestay> findByNameContainingIgnoreCase(String name);

    List<Homestay> findByLocationIgnoreCase(String location);

    List<Homestay> findByPricePerNightBetween(double minPrice, double maxPrice);

    List<Homestay> findByOwner(User owner);

    List<Homestay> findByOwnerId(Long ownerId); // Thêm phương thức mới

    List<Homestay> findByStatus(HomestayStatus status);

    @Query("SELECT h FROM Homestay h WHERE " +
            "(:destination IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :destination, '%')) " +
            "OR LOWER(h.location) LIKE LOWER(CONCAT('%', :destination, '%'))) " +
            "AND (:priceLimit IS NULL OR h.pricePerNight <= :priceLimit)")
    List<Homestay> findHomestaysByCriteria(
            @Param("destination") String destination,
            @Param("priceLimit") Double priceLimit);
}