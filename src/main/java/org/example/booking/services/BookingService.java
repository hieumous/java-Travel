package org.example.booking.services;

import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.models.User;
import org.example.booking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    //Tao booking moi
    public Booking createBooking(Homestay homestay, User user, LocalDate checkInDate, LocalDate checkOutDate, boolean isPaid) {
        // Kiem tra ngay check-out phai sau ngay check-in
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        Booking booking = new Booking(homestay, user, checkInDate, checkOutDate, isPaid);
        return bookingRepository.save(booking);
    }

    //Lay danh sach booking theo homestay
    public List<Booking> getBookingsByHomestay(Long homestayId) {
        return bookingRepository.findByHomestayId(homestayId);
    }
    // lay danh sach booking theo userid
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }


}
