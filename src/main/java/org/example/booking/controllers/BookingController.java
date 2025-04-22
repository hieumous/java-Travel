package org.example.booking.controllers;

import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.services.BookingService;
import org.example.booking.services.EmailService;
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

    @Autowired
    private EmailService emailService;

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
            Homestay homestay = homestayService.findById(homestayId);
            String homestayName = homestay.getName(); // hoặc getTitle() nếu bạn dùng tên khác

            // Chuyển đổi ngày từ String sang LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkIn = LocalDate.parse(checkinDate, formatter);
            LocalDate checkOut = LocalDate.parse(checkoutDate, formatter);

            // Tạo booking
            Booking booking = bookingService.createBookingFromForm(
                    homestayId, username, email, phone, checkIn, checkOut
            );

            // Gửi email xác nhận đặt phòng có mã QR
            String subject = "Xác nhận đặt phòng HomeStay " + homestayName;

            String body = "Xin chào " + username + ",<br><br>"
                    + "Bạn đã đặt HomeStay <strong> " + homestayName + " thành công</strong>.<br>"
                    + "Thời gian: từ <b>" + checkinDate + "</b> đến <b>" + checkoutDate + "</b>.<br><br>"
                    + "Vui lòng quét mã QR bên dưới để xác nhận khi đến nơi.<br>"
                    + "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!";

            String qrContent = "Homestay: " + homestayName + "\nTên: " + username
                    + "\nCheck-in: " + checkinDate + "\nCheck-out: " + checkoutDate;


            emailService.sendEmailWithQR(email, subject, body, qrContent);

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