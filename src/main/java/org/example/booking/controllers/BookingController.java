package org.example.booking.controllers;

import org.example.booking.enums.BookingStatus;
import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.models.PaymentInfo;
import org.example.booking.models.PaymentStatus;
import org.example.booking.models.User;
import org.example.booking.services.BookingService;
import org.example.booking.services.EmailService;
import org.example.booking.services.HomestayService;
import org.example.booking.services.PaymentService;
import org.example.booking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private HomestayService homestayService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    // Hiển thị form đặt phòng
    @GetMapping("/booking")
    public String showBookingForm(
            @RequestParam(required = false) Long homestayId,
            Model model,
            Authentication authentication) {
        // Kiểm tra đăng nhập
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login?bookingRequired=true";
        }
        // Kiểm tra vai trò CUSTOMER
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"));
        if (!isCustomer) {
            model.addAttribute("error", "Bạn cần tài khoản CUSTOMER để đặt phòng.");
            return "login";
        }

        // Kiểm tra homestayId
        if (homestayId == null) {
            model.addAttribute("error", "Không tìm thấy homestay. Vui lòng chọn homestay để đặt phòng.");
            return "error";
        }

        // Lấy thông tin người dùng để điền mặc định
        Optional<User> user = userService.findByUsername(authentication.getName());
        model.addAttribute("homestayId", homestayId);
        model.addAttribute("booking", new Booking());
        model.addAttribute("defaultName", user.get().getUsername());
        model.addAttribute("defaultEmail", user.get().getEmail() != null ? user.get().getEmail() : "");
        model.addAttribute("defaultPhone", "");
        return "booking";
    }

    // Xử lý form đặt phòng
    @PostMapping("/booking")
    public String processBooking(
            @RequestParam(required = false) Long homestayId,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String checkinDate,
            @RequestParam String checkoutDate,
            Authentication authentication,
            Model model) {
        try {
            // Kiểm tra đăng nhập
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?bookingRequired=true";
            }

            // Kiểm tra homestayId
            if (homestayId == null) {
                model.addAttribute("error", "Không tìm thấy homestay. Vui lòng chọn homestay để đặt phòng.");
                return "booking";
            }

            Homestay homestay = homestayService.findById(homestayId);
            if (homestay == null) {
                throw new IllegalArgumentException("Homestay không tìm thấy.");
            }

            // Kiểm tra định dạng email
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                model.addAttribute("error", "Email không hợp lệ.");
                model.addAttribute("homestayId", homestayId);
                return "booking";
            }

            // Chuẩn hóa số điện thoại
            String cleanedPhone = phone.replaceAll("[\\s-+]", "");
            if (!cleanedPhone.matches("^[0-9]{10,12}$")) {
                model.addAttribute("error", "Số điện thoại không hợp lệ. Vui lòng nhập 10-12 chữ số.");
                model.addAttribute("homestayId", homestayId);
                return "booking";
            }

            // Chuyển đổi ngày
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkIn = LocalDate.parse(checkinDate, formatter);
            LocalDate checkOut = LocalDate.parse(checkoutDate, formatter);

            // Kiểm tra ngày hợp lệ
            if (checkOut.isBefore(checkIn) || checkIn.isEqual(checkOut)) {
                model.addAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng.");
                model.addAttribute("homestayId", homestayId);
                return "booking";
            }

            // Tính số tiền và số đêm
            double amount = bookingService.calculateTotalAmount(homestayId, checkIn, checkOut);
            long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);

            // Tạo booking
            Optional<User> user = userService.findByUsername(authentication.getName());
            Booking booking = bookingService.createBookingFromForm(
                    homestayId, name, email, cleanedPhone, checkIn, checkOut, user.orElse(null)
            );

            // Chuyển hướng đến trang thanh toán
            model.addAttribute("bookingId", booking.getId());
            model.addAttribute("homestayId", homestayId);
            model.addAttribute("name", homestay.getName());
            model.addAttribute("image", homestay.getImage());
            model.addAttribute("pricePerNight", homestay.getPricePerNight());
            model.addAttribute("location", homestay.getLocation());
            model.addAttribute("amenities", homestay.getAmenities());
            model.addAttribute("checkInDate", checkIn.toString());
            model.addAttribute("checkOutDate", checkOut.toString());
            model.addAttribute("numberOfNights", numberOfNights);
            model.addAttribute("amount", amount);
            model.addAttribute("currency", "USD");
            return "payment";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("homestayId", homestayId);
            return "booking";
        }
    }

    // Hiển thị trang thanh toán
    @GetMapping("/payment")
    public String showPaymentForm(
            @RequestParam Long bookingId,
            @RequestParam Long homestayId,
            @RequestParam String name,
            @RequestParam String image,
            @RequestParam double pricePerNight,
            @RequestParam String location,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam long numberOfNights,
            @RequestParam double amount,
            @RequestParam String currency,
            Model model) {
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("homestayId", homestayId);
        model.addAttribute("name", name);
        model.addAttribute("image", image);
        model.addAttribute("pricePerNight", pricePerNight);
        model.addAttribute("location", location);
        model.addAttribute("amenities", homestayService.findById(homestayId).getAmenities());
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);
        model.addAttribute("numberOfNights", numberOfNights);
        model.addAttribute("amount", amount);
        model.addAttribute("currency", currency);
        return "payment";
    }

    // Xử lý thanh toán giả lập
    @PostMapping("/simulate-payment")
    public String simulatePayment(
            @RequestParam Long bookingId,
            @RequestParam Long homestayId,
            @RequestParam double amount,
            @RequestParam String currency,
            @RequestParam String paymentMethod,
            Authentication authentication,
            Model model) {
        try {
            // Kiểm tra đăng nhập
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?bookingRequired=true";
            }

            Homestay homestay = homestayService.findById(homestayId);
            if (homestay == null) {
                throw new IllegalArgumentException("Homestay không tìm thấy.");
            }
            Booking booking = bookingService.updateBookingStatus(bookingId, BookingStatus.PENDING, false);

            // Kiểm tra quyền sở hữu
            if (!booking.getUser().getUsername().equals(authentication.getName())) {
                throw new IllegalArgumentException("Bạn không có quyền thực hiện thanh toán cho đặt phòng này.");
            }

            // Tạo PaymentInfo
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setBookingId(bookingId);
            paymentInfo.setHomestayId(homestayId);
            paymentInfo.setAmount(amount);
            paymentInfo.setCurrency(currency);
            paymentInfo.setPaymentMethod(org.example.booking.enums.PaymentMethod.valueOf(paymentMethod));
            paymentInfo.setUser(booking.getUser());

            // Gọi thanh toán giả lập
            PaymentStatus paymentStatus = paymentService.processPayment(paymentInfo);

            if (paymentStatus.getStatus() == PaymentStatus.PaymentStatusType.SUCCESSFUL) {
                // Cập nhật booking
                bookingService.updateBookingStatus(bookingId, BookingStatus.CONFIRMED, true);

                // Gửi email xác nhận
                String subject = "Xác nhận đặt phòng HomeStay " + homestay.getName();
                String body = "Xin chào " + booking.getName() + ",<br><br>"
                        + "Bạn đã đặt HomeStay <strong>" + homestay.getName() + "</strong> thành công.<br>"
                        + "Thời gian: từ <b>" + booking.getCheckInDate() + "</b> đến <b>" + booking.getCheckOutDate() + "</b>.<br>"
                        + "Thanh toán: <b>" + amount + " " + currency + "</b> (Thành công).<br><br>"
                        + "Vui lòng quét mã QR bên dưới để xác nhận khi đến nơi.<br>"
                        + "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!";
                String qrContent = "Homestay: " + homestay.getName() + "\nTên: " + booking.getName()
                        + "\nCheck-in: " + booking.getCheckInDate() + "\nCheck-out: " + booking.getCheckOutDate();

                emailService.sendEmailWithQR(booking.getEmail(), subject, body, qrContent);
                return "redirect:/booking-success";
            } else {
                model.addAttribute("error", "Thanh toán thất bại: " + paymentStatus.getErrorMessage());
                model.addAttribute("bookingId", bookingId);
                model.addAttribute("homestayId", homestayId);
                model.addAttribute("name", homestay.getName());
                model.addAttribute("image", homestay.getImage());
                model.addAttribute("pricePerNight", homestay.getPricePerNight());
                model.addAttribute("location", homestay.getLocation());
                model.addAttribute("amenities", homestay.getAmenities());
                model.addAttribute("checkInDate", booking.getCheckInDate().toString());
                model.addAttribute("checkOutDate", booking.getCheckOutDate().toString());
                model.addAttribute("numberOfNights", ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate()));
                model.addAttribute("amount", amount);
                model.addAttribute("currency", currency);
                return "payment";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("homestayId", homestayId);
            model.addAttribute("name", homestayService.findById(homestayId).getName());
            model.addAttribute("image", homestayService.findById(homestayId).getImage());
            model.addAttribute("pricePerNight", homestayService.findById(homestayId).getPricePerNight());
            model.addAttribute("location", homestayService.findById(homestayId).getLocation());
            model.addAttribute("amenities", homestayService.findById(homestayId).getAmenities());
            model.addAttribute("checkInDate", bookingService.updateBookingStatus(bookingId, BookingStatus.PENDING, false).getCheckInDate().toString());
            model.addAttribute("checkOutDate", bookingService.updateBookingStatus(bookingId, BookingStatus.PENDING, false).getCheckOutDate().toString());
            model.addAttribute("numberOfNights", ChronoUnit.DAYS.between(
                    bookingService.updateBookingStatus(bookingId, BookingStatus.PENDING, false).getCheckInDate(),
                    bookingService.updateBookingStatus(bookingId, BookingStatus.PENDING, false).getCheckOutDate()));
            model.addAttribute("amount", amount);
            model.addAttribute("currency", currency);
            return "payment";
        }
    }

    // Hiển thị danh sách đặt phòng cho admin
    @GetMapping("/admin/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAllBookings(Model model, Authentication authentication) {
        try {
            // Kiểm tra đăng nhập và vai trò admin
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?adminRequired=true";
            }
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                model.addAttribute("error", "Bạn cần tài khoản ADMIN để xem danh sách đặt phòng.");
                return "login";
            }

            // Lấy danh sách tất cả đặt phòng
            List<Booking> bookings = bookingService.getAllBookings();
            model.addAttribute("bookings", bookings);
            return "admin-bookings";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải danh sách đặt phòng: " + e.getMessage());
            return "error";
        }
    }

    // Trang xác nhận đặt phòng thành công
    @GetMapping("/booking-success")
    public String bookingSuccess() {
        return "booking-success";
    }


}