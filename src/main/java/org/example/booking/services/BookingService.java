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
    private HomestayService homestayService;

    public Booking createBooking(Homestay homestay, User user, LocalDate checkInDate, LocalDate checkOutDate, boolean isPaid) {
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Ngày trả phòng phải sau ngày nhận phòng.");
        }
        Booking booking = new Booking(homestay, user, checkInDate, checkOutDate, isPaid);
        return bookingRepository.save(booking);
    }

    public Booking createBookingFromForm(Long homestayId, String name, String email, String phone,
                                         LocalDate checkInDate, LocalDate checkOutDate, User user) {
        // Tìm homestay
        Homestay homestay = homestayService.findById(homestayId);
        if (homestay == null) {
            throw new IllegalArgumentException("Homestay không tìm thấy: " + homestayId);
        }

        // Tạo booking với user được truyền vào
        Booking booking = new Booking(homestay, user, checkInDate, checkOutDate, false);
        booking.setName(name);
        booking.setEmail(email);
        booking.setPhone(phone);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByHomestay(Long homestayId) {
        return bookingRepository.findByHomestayId(homestayId);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking updateBookingStatus(Long bookingId, BookingStatus status, boolean isPaid) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Đặt phòng không tìm thấy."));
        booking.setStatus(status);
        booking.setPaid(isPaid);
        return bookingRepository.save(booking);
    }

    public double calculateTotalAmount(Long homestayId, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
            throw new IllegalArgumentException("Ngày nhận phòng hoặc trả phòng không hợp lệ.");
        }
        Homestay homestay = homestayService.findById(homestayId);
        if (homestay == null) {
            throw new IllegalArgumentException("Homestay không tìm thấy.");
        }
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return homestay.getPricePerNight() * numberOfNights;
    }
}