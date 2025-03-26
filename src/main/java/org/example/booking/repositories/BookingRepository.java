package org.example.booking.repositories;

import org.example.booking.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long id);
    List<Booking> findByHomestayId(Long id);
}
