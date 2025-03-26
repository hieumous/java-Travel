package org.example.booking.repositories;

import org.example.booking.models.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HomestayRepository extends JpaRepository<Homestay, Long> {

    // Tim kiem theo ten homestay
    List<Homestay> findByNameContainingIgnoreCase(String name);

    //Tim theo location
    List<Homestay> findByLocationIgnoreCase(String location);

    // Tim theo gia
    List<Homestay> findByPricePerNightBetween(double minPrice, double maxPrice);
}
