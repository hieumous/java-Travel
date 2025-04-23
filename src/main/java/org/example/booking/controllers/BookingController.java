package org.example.booking.controllers;

import org.example.booking.enums.BookingStatus;
import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.models.ListFood;
import org.example.booking.models.PaymentInfo;
import org.example.booking.models.PaymentStatus;
import org.example.booking.models.User;
import org.example.booking.repositories.FoodRepository;
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
import java.util.ArrayList;
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

    @Autowired
    private FoodRepository foodRepository;

    // Hiển thị form đặt phòng
    @GetMapping("/booking")
    public String showBookingForm(
            @RequestParam(required = false) Long homestayId,
            Model model,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login?bookingRequired=true";
        }
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"));
        if (!isCustomer) {
            model.addAttribute("error", "Bạn cần tài khoản CUSTOMER để đặt phòng.");
            return "login";
        }
        if (homestayId == null) {
            model.addAttribute("error", "Không tìm thấy homestay. Vui lòng chọn homestay để đặt phòng.");
            return "error";
        }
        Homestay homestay = homestayService.findById(homestayId);
        if (homestay == null) {
            model.addAttribute("error", "Homestay không tồn tại.");
            return "error";
        }
        Optional<User> user = userService.findByUsername(authentication.getName());
        model.addAttribute("homestayId", homestayId);
        model.addAttribute("booking", new Booking());
        model.addAttribute("defaultName", user.get().getUsername());
        model.addAttribute("defaultEmail", user.get().getEmail() != null ? user.get().getEmail() : "");
        model.addAttribute("defaultPhone", "");
        model.addAttribute("services", foodRepository.findByHomestayId(homestayId)); // Sửa ở đây
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
            @RequestParam(required = false) List<Long> serviceIds,
            Authentication authentication,
            Model model) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?bookingRequired=true";
            }
            if (homestayId == null) {
                model.addAttribute("error", "Không tìm thấy homestay. Vui lòng chọn homestay để đặt phòng.");
                model.addAttribute("homestayId", homestayId);
                model.addAttribute("services", homestayId != null ? foodRepository.findByHomestayId(homestayId) : new ArrayList<>());
                return "booking";
            }
            Homestay homestay = homestayService.findById(homestayId);
            if (homestay == null) {
                throw new IllegalArgumentException("Homestay không tìm thấy.");
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                model.addAttribute("error", "Email không hợp lệ.");
                model.addAttribute("homestayId", homestayId);
                model.addAttribute("services", foodRepository.findByHomestayId(homestayId));
                return "booking";
            }
            String cleanedPhone = phone.replaceAll("[\\s-+]", "");
            if (!cleanedPhone.matches("^[0-9]{10,12}$")) {
                model.addAttribute("error", "Số điện thoại không hợp lệ. Vui lòng nhập 10-12 chữ số.");
                model.addAttribute("homestayId", homestayId);
                model.addAttribute("services", foodRepository.findByHomestayId(homestayId));
                return "booking";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkIn = LocalDate.parse(checkinDate, formatter);
            LocalDate checkOut = LocalDate.parse(checkoutDate, formatter);
            if (checkOut.isBefore(checkIn) || checkIn.isEqual(checkOut)) {
                model.addAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng.");
                model.addAttribute("homestayId", homestayId);
                model.addAttribute("services", foodRepository.findByHomestayId(homestayId));
                return "booking";
            }
            double roomAmount = bookingService.calculateTotalAmount(homestayId, checkIn, checkOut);
            List<ListFood> selectedServices = serviceIds != null ? foodRepository.findAllById(serviceIds) : List.of();
            double serviceAmount = selectedServices.stream().mapToDouble(ListFood::getPrice).sum();
            double totalAmount = roomAmount + serviceAmount;
            long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
            Optional<User> user = userService.findByUsername(authentication.getName());
            Booking booking = bookingService.createBookingFromForm(
                    homestayId, name, email, cleanedPhone, checkIn, checkOut, user.orElse(null)
            );
            bookingService.saveSelectedServices(booking.getId(), serviceIds);

            // Gửi email xác nhận đăng ký
            String subject = "Xác nhận đăng ký đặt phòng HomeStay " + homestay.getName();
            StringBuilder body = new StringBuilder();
            body.append("Xin chào ").append(booking.getName()).append(",<br><br>")
                    .append("Bạn đã đăng ký đặt phòng <strong>").append(homestay.getName()).append("</strong> thành công.<br>")
                    .append("Thời gian: từ <b>").append(booking.getCheckInDate()).append("</b> đến <b>")
                    .append(booking.getCheckOutDate()).append("</b>.<br>")
                    .append("Tiền phòng: <b>").append(roomAmount).append(" USD</b>.<br>");
            if (!selectedServices.isEmpty()) {
                body.append("Dịch vụ:<br><ul>");
                for (ListFood service : selectedServices) {
                    body.append("<li>").append(service.getNameFood()).append(": ").append(service.getPrice()).append(" USD</li>");
                }
                body.append("</ul>");
                body.append("Tiền dịch vụ: <b>").append(serviceAmount).append(" USD</b>.<br>");
            }
            body.append("Tổng tiền: <b>").append(totalAmount).append(" USD</b> (Chưa thanh toán).<br><br>")
                    .append("Vui lòng thanh toán để xác nhận đặt phòng.<br>")
                    .append("Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!");
            emailService.sendEmail(booking.getEmail(), subject, body.toString());

            // Chuyển đến trang xác nhận với tùy chọn thanh toán
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
            model.addAttribute("roomAmount", roomAmount);
            model.addAttribute("serviceAmount", serviceAmount);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("selectedServices", selectedServices);
            model.addAttribute("currency", "USD");
            return "confirmation"; // Sửa tên template để khớp với file confirmation.html
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("homestayId", homestayId);
            model.addAttribute("services", homestayId != null ? foodRepository.findByHomestayId(homestayId) : new ArrayList<>());
            return "booking";
        }
    }

    // Hiển thị trang thanh toán từ profile
    @GetMapping("/payment/{bookingId}")
    public String showPaymentForm(@PathVariable Long bookingId, Model model, Authentication authentication) {
        // Khởi tạo các biến cần thiết với giá trị mặc định
        double roomAmount = 0.0;
        double serviceAmount = 0.0;
        double totalAmount = 0.0;
        List<ListFood> selectedServices = new ArrayList<>();
        long numberOfNights = 0;
        Homestay homestay = null;
        Booking booking = null;

        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?bookingRequired=true";
            }
            booking = bookingService.getBookingById(bookingId);
            if (!booking.getUser().getUsername().equals(authentication.getName())) {
                model.addAttribute("error", "Bạn không có quyền thanh toán cho đặt phòng này.");
                return "error";
            }
            homestay = booking.getHomestay();
            roomAmount = bookingService.calculateTotalAmount(homestay.getId(), booking.getCheckInDate(), booking.getCheckOutDate());
            selectedServices = bookingService.getSelectedServices(bookingId);
            serviceAmount = selectedServices.stream().mapToDouble(ListFood::getPrice).sum();
            totalAmount = roomAmount + serviceAmount;
            numberOfNights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());

            model.addAttribute("bookingId", bookingId);
            model.addAttribute("homestayId", homestay.getId());
            model.addAttribute("name", homestay.getName());
            model.addAttribute("image", homestay.getImage());
            model.addAttribute("pricePerNight", homestay.getPricePerNight());
            model.addAttribute("location", homestay.getLocation());
            model.addAttribute("amenities", homestay.getAmenities());
            model.addAttribute("checkInDate", booking.getCheckInDate().toString());
            model.addAttribute("checkOutDate", booking.getCheckOutDate().toString());
            model.addAttribute("numberOfNights", numberOfNights);
            model.addAttribute("roomAmount", roomAmount);
            model.addAttribute("serviceAmount", serviceAmount);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("selectedServices", selectedServices);
            model.addAttribute("currency", "USD");
            return "payment";
        } catch (Exception e) {
            // Trong trường hợp có lỗi, sử dụng booking và homestay nếu chúng đã được khởi tạo
            if (booking != null && homestay != null) {
                // Tính lại các giá trị nếu có thể
                roomAmount = bookingService.calculateTotalAmount(homestay.getId(), booking.getCheckInDate(), booking.getCheckOutDate());
                selectedServices = bookingService.getSelectedServices(bookingId);
                serviceAmount = selectedServices.stream().mapToDouble(ListFood::getPrice).sum();
                totalAmount = roomAmount + serviceAmount;
                numberOfNights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
            }
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("homestayId", homestay != null ? homestay.getId() : 0L);
            model.addAttribute("name", homestay != null ? homestay.getName() : "N/A");
            model.addAttribute("image", homestay != null ? homestay.getImage() : null);
            model.addAttribute("pricePerNight", homestay != null ? homestay.getPricePerNight() : 0.0);
            model.addAttribute("location", homestay != null ? homestay.getLocation() : "N/A");
            model.addAttribute("amenities", homestay != null ? homestay.getAmenities() : new ArrayList<>());
            model.addAttribute("checkInDate", booking != null ? booking.getCheckInDate().toString() : "N/A");
            model.addAttribute("checkOutDate", booking != null ? booking.getCheckOutDate().toString() : "N/A");
            model.addAttribute("numberOfNights", numberOfNights);
            model.addAttribute("roomAmount", roomAmount);
            model.addAttribute("serviceAmount", serviceAmount);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("selectedServices", selectedServices);
            model.addAttribute("currency", "USD");
            return "payment"; // Trả về trang payment để hiển thị lỗi thay vì "error"
        }
    }

    // Xử lý thanh toán giả lập
    @PostMapping("/simulate-payment")
    public String simulatePayment(
            @RequestParam Long bookingId,
            @RequestParam Long homestayId,
            @RequestParam double totalAmount,
            @RequestParam String currency,
            @RequestParam String paymentMethod,
            Authentication authentication,
            Model model) {
        double roomAmount = 0.0;
        double serviceAmount = 0.0;
        List<ListFood> selectedServices = new ArrayList<>();

        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?bookingRequired=true";
            }
            Homestay homestay = homestayService.findById(homestayId);
            if (homestay == null) {
                throw new IllegalArgumentException("Homestay không tìm thấy.");
            }
            Booking booking = bookingService.getBookingById(bookingId);
            if (!booking.getUser().getUsername().equals(authentication.getName())) {
                throw new IllegalArgumentException("Bạn không có quyền thực hiện thanh toán cho đặt phòng này.");
            }
            roomAmount = bookingService.calculateTotalAmount(homestay.getId(), booking.getCheckInDate(), booking.getCheckOutDate());
            selectedServices = bookingService.getSelectedServices(bookingId);
            serviceAmount = selectedServices.stream().mapToDouble(ListFood::getPrice).sum();
            double calculatedTotal = roomAmount + serviceAmount;
            if (Math.abs(totalAmount - calculatedTotal) > 0.01) {
                throw new IllegalArgumentException("Số tiền thanh toán không khớp với tổng tiền đặt phòng.");
            }

            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setBookingId(bookingId);
            paymentInfo.setHomestayId(homestayId);
            paymentInfo.setAmount(totalAmount);
            paymentInfo.setCurrency(currency);
            paymentInfo.setPaymentMethod(org.example.booking.enums.PaymentMethod.valueOf(paymentMethod));
            paymentInfo.setUser(booking.getUser());
            PaymentStatus paymentStatus = paymentService.processPayment(paymentInfo);

            if (paymentStatus.getStatus() == PaymentStatus.PaymentStatusType.SUCCESSFUL) {
                bookingService.updateBookingStatus(bookingId, BookingStatus.CONFIRMED, true);
                String subject = "Xác nhận thanh toán HomeStay " + homestay.getName();
                StringBuilder body = new StringBuilder();
                body.append("Xin chào ").append(booking.getName()).append(",<br><br>")
                        .append("Bạn đã thanh toán cho đặt phòng <strong>").append(homestay.getName()).append("</strong> thành công.<br>")
                        .append("Thời gian: từ <b>").append(booking.getCheckInDate()).append("</b> đến <b>")
                        .append(booking.getCheckOutDate()).append("</b>.<br>")
                        .append("Tiền phòng: <b>").append(roomAmount).append(" USD</b>.<br>");
                if (!selectedServices.isEmpty()) {
                    body.append("Dịch vụ:<br><ul>");
                    for (ListFood service : selectedServices) {
                        body.append("<li>").append(service.getNameFood()).append(": ").append(service.getPrice()).append(" USD</li>");
                    }
                    body.append("</ul>");
                    body.append("Tiền dịch vụ: <b>").append(serviceAmount).append(" USD</b>.<br>");
                }
                body.append("Thanh toán: <b>").append(totalAmount).append(" ").append(currency).append("</b> (Thành công).<br><br>")
                        .append("Vui lòng quét mã QR bên dưới để xác nhận khi đến nơi.<br>")
                        .append("Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!");
                String qrContent = "Homestay: " + homestay.getName() + "\nTên: " + booking.getName()
                        + "\nCheck-in: " + booking.getCheckInDate() + "\nCheck-out: " + booking.getCheckOutDate();
                emailService.sendEmailWithQR(booking.getEmail(), subject, body.toString(), qrContent);
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
                model.addAttribute("roomAmount", roomAmount);
                model.addAttribute("serviceAmount", serviceAmount);
                model.addAttribute("totalAmount", totalAmount);
                model.addAttribute("selectedServices", selectedServices);
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
            model.addAttribute("checkInDate", bookingService.getBookingById(bookingId).getCheckInDate().toString());
            model.addAttribute("checkOutDate", bookingService.getBookingById(bookingId).getCheckOutDate().toString());
            model.addAttribute("numberOfNights", ChronoUnit.DAYS.between(
                    bookingService.getBookingById(bookingId).getCheckInDate(),
                    bookingService.getBookingById(bookingId).getCheckOutDate()));
            model.addAttribute("roomAmount", roomAmount);
            model.addAttribute("serviceAmount", serviceAmount);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("selectedServices", selectedServices);
            model.addAttribute("currency", currency);
            return "payment";
        }
    }

    // Hiển thị chi tiết đặt phòng
    @GetMapping("/booking/{id}")
    public String showBookingDetail(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?bookingRequired=true";
            }
            Booking booking = bookingService.getBookingById(id);
            if (!booking.getUser().getUsername().equals(authentication.getName()) &&
                    !authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("error", "Bạn không có quyền xem chi tiết đặt phòng này.");
                return "error";
            }
            double roomAmount = bookingService.calculateTotalAmount(booking.getHomestay().getId(), booking.getCheckInDate(), booking.getCheckOutDate());
            List<ListFood> selectedServices = bookingService.getSelectedServices(id);
            double serviceAmount = selectedServices.stream().mapToDouble(ListFood::getPrice).sum();
            double totalAmount = roomAmount + serviceAmount;

            model.addAttribute("booking", booking);
            model.addAttribute("homestay", booking.getHomestay());
            model.addAttribute("numberOfNights", ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate()));
            model.addAttribute("roomAmount", roomAmount);
            model.addAttribute("serviceAmount", serviceAmount);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("selectedServices", selectedServices);
            return "booking-detail";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải chi tiết đặt phòng: " + e.getMessage());
            return "error";
        }
    }

    // Hủy đặt phòng
    @GetMapping("/booking/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login?bookingRequired=true";
            }
            Booking booking = bookingService.getBookingById(id);
            if (!booking.getUser().getUsername().equals(authentication.getName())) {
                model.addAttribute("error", "Bạn không có quyền hủy đặt phòng này.");
                return "error";
            }
            bookingService.cancelBooking(id);
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi hủy đặt phòng: " + e.getMessage());
            return "error";
        }
    }

    // Trang xác nhận đặt phòng thành công
    @GetMapping("/booking-success")
    public String bookingSuccess() {
        return "booking-success";
    }
}