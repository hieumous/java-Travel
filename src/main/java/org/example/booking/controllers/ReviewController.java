package org.example.booking.controllers;

import org.example.booking.models.Review;
import org.example.booking.models.Booking;
import org.example.booking.repositories.BookingRepository;
import org.example.booking.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final BookingRepository bookingRepository;
    private final ReviewService reviewService;

    @GetMapping("/danhgia")
    public String showReviewPage(Model model, HttpSession session, Authentication authentication) {
        Long bookingId = (Long) session.getAttribute("bookingId");
        if (bookingId == null) {
            model.addAttribute("error", "Không tìm thấy thông tin đặt phòng.");
            return "danhgia";
        }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            model.addAttribute("error", "Không tìm thấy thông tin đặt phòng.");
            return "danhgia";
        }

        // Lấy userId từ session hoặc từ authentication
        Long userId = null;
        if(authentication != null && authentication.getPrincipal() instanceof org.example.booking.models.User) {
            org.example.booking.models.User user = (org.example.booking.models.User) authentication.getPrincipal();
            userId = user.getId();
        }

        if (userId == null) {
            model.addAttribute("error", "Bạn chưa đăng nhập.");
            return "danhgia";
        }

        // Chuẩn bị dữ liệu
        model.addAttribute("homestayId", booking.getHomestay().getId());
        model.addAttribute("userId", userId);
        model.addAttribute("review", new Review());

        return "danhgia";
    }

    @PostMapping("/danhgia")
    public String submitReview(@RequestParam("homestayId") Long homestayId,
                               @RequestParam("userId") Long userId,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment,
                               RedirectAttributes redirectAttributes) {
        try {
            reviewService.createReview(homestayId, userId, rating, comment);
            redirectAttributes.addFlashAttribute("message", "Cảm ơn bạn đã đánh giá!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

}
