package org.example.booking.services;

import org.example.booking.enums.BookingStatus;
import org.example.booking.enums.Role;
import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.models.User;
import org.example.booking.repositories.BookingRepository;
import org.example.booking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomestayService homestayService; // Inject HomestayService

    public Booking createBooking(Homestay homestay, User user, LocalDate checkInDate, LocalDate checkOutDate, boolean isPaid) {
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        Booking booking = new Booking(homestay, user, checkInDate, checkOutDate, isPaid);
        return bookingRepository.save(booking);
    }

    public Booking createBookingFromForm(Long homestayId, String username, String email, String phone,
                                         LocalDate checkInDate, LocalDate checkOutDate) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword("defaultPassword"); // Cần xử lý password tốt hơn
            user.setRole(Role.CUSTOMER);
            userRepository.save(user);
        }

        Homestay homestay = homestayService.findById(homestayId); // Sử dụng HomestayService để lấy Homestay

        return createBooking(homestay, user, checkInDate, checkOutDate, false);
    }

    public List<Booking> getBookingsByHomestay(Long homestayId) {
        return bookingRepository.findByHomestayId(homestayId);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking updateBookingStatus(Long bookingId, BookingStatus status, boolean isPaid) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus(status);
        booking.setPaid(isPaid);
        return bookingRepository.save(booking);
    }

    /**
     * Tính tổng số tiền cho đặt phòng dựa trên homestayId và khoảng thời gian.
     *
     * @param homestayId ID của homestay
     * @param checkIn    Ngày nhận phòng
     * @param checkOut   Ngày trả phòng
     * @return Tổng số tiền
     * @throws IllegalArgumentException nếu homestay không tồn tại hoặc ngày không hợp lệ
     */
    public double calculateTotalAmount(Long homestayId, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
            throw new IllegalArgumentException("Invalid check-in or check-out date.");
        }

        Homestay homestay = homestayService.findById(homestayId);
        if (homestay == null) {
            throw new IllegalArgumentException("Homestay not found.");
        }

        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return homestay.getPricePerNight() * numberOfNights;
    }
}