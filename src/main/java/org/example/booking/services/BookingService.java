package org.example.booking.services;

import org.example.booking.enums.Role;
import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.models.User;
import org.example.booking.repositories.BookingRepository;
import org.example.booking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(Homestay homestay, User user, LocalDate checkInDate, LocalDate checkOutDate, boolean isPaid) {
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        Booking booking = new Booking(homestay, user, checkInDate, checkOutDate, isPaid);
        return bookingRepository.save(booking);
    }

    // Thêm phương thức tạo booking từ form
    public Booking createBookingFromForm(Long homestayId, String username, String email, String phone,
                                         LocalDate checkInDate, LocalDate checkOutDate) {
        // Tìm hoặc tạo user
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword("defaultPassword"); // Cần xử lý password tốt hơn trong thực tế
            user.setRole(Role.CUSTOMER); // Giả định role
            userRepository.save(user);
        }

        // Tìm homestay
        Homestay homestay = new Homestay();
        homestay.setId(homestayId); // Sẽ được gán sau khi tích hợp với HomestayService

        // Tạo booking
        return createBooking(homestay, user, checkInDate, checkOutDate, false);
    }

    public List<Booking> getBookingsByHomestay(Long homestayId) {
        return bookingRepository.findByHomestayId(homestayId);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}