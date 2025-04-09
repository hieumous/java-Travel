    package org.example.booking.repositories;
    import java.time.LocalDate;
    import org.example.booking.models.Booking;
    import org.example.booking.models.User;
    import org.example.booking.models.Homestay;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import java.util.List;

    @Repository
    public interface BookingRepository extends JpaRepository<Booking, Long> {
        List<Booking> findByUserId(Long id);
        List<Booking> findByHomestayId(Long id);
        List<Booking> findByUser(User user);
        List<Booking> findByHomestay(Homestay homestay);
        List<Booking> findByCheckInDateBetween(LocalDate start, LocalDate end);
        List<Booking> findByHomestayIdAndMonth(Long homestayId, int month);
    }
