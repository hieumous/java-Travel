package org.example.booking.controllers;

import org.example.booking.models.Booking;
import org.example.booking.services.BookingService;
import org.example.booking.services.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private HomestayService homestayService;

    // Hiển thị form đặt phòng
    @GetMapping("/booking")
    public String showBookingForm(@RequestParam Long homestayId, Model model) {
        model.addAttribute("homestayId", homestayId);
        model.addAttribute("booking", new Booking());
        return "booking";
    }

    // Xử lý form đặt phòng
    @PostMapping("/booking")
    public String processBooking(
            @RequestParam Long homestayId,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String checkinDate,
            @RequestParam String checkoutDate,
            Model model) {
        try {
            // Chuyển đổi ngày từ String sang LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkIn = LocalDate.parse(checkinDate, formatter);
            LocalDate checkOut = LocalDate.parse(checkoutDate, formatter);

            // Tạo booking
            Booking booking = bookingService.createBookingFromForm(
                    homestayId, username, email, phone, checkIn, checkOut
            );

            return "redirect:/booking-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("homestayId", homestayId);
            return "booking";
        }
    }

    // Trang xác nhận đặt phòng thành công
    @GetMapping("/booking-success")
    public String bookingSuccess() {
        return "booking-success";
    }
}