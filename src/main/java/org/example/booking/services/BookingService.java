package org.example.booking.services;

import org.example.booking.enums.BookingStatus;
import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.models.ListFood;
import org.example.booking.models.User;
import org.example.booking.repositories.BookingRepository;
import org.example.booking.repositories.FoodRepository;
import org.example.booking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomestayService homestayService;

    @Autowired
    private FoodRepository foodRepository;

    public Booking createBooking(Homestay homestay, User user, LocalDate checkInDate, LocalDate checkOutDate, boolean isPaid) {
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Ngày trả phòng phải sau ngày nhận phòng.");
        }
        Booking booking = new Booking(homestay, user, checkInDate, checkOutDate, isPaid);
        return bookingRepository.save(booking);
    }

    public Booking createBookingFromForm(Long homestayId, String name, String email, String phone,
                                         LocalDate checkInDate, LocalDate checkOutDate, User user) {
        Homestay homestay = homestayService.findById(homestayId);
        if (homestay == null) {
            throw new IllegalArgumentException("Homestay không tìm thấy: " + homestayId);
        }
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

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đặt phòng không tồn tại"));
    }

    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() == BookingStatus.PENDING) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Chỉ có thể hủy đặt phòng đang chờ");
        }
    }

    public void saveSelectedServices(Long bookingId, List<Long> serviceIds) {
        Booking booking = getBookingById(bookingId);
        List<ListFood> services = serviceIds != null && !serviceIds.isEmpty()
                ? foodRepository.findAllById(serviceIds)
                : new ArrayList<>(); // Sử dụng ArrayList mutable
        booking.getServices().clear(); // Xóa dịch vụ hiện tại
        booking.getServices().addAll(services); // Thêm dịch vụ mới
        bookingRepository.save(booking);
    }

    public List<ListFood> getSelectedServices(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        return booking.getServices();
    }
}