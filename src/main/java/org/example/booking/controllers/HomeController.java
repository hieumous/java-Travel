package org.example.booking.controllers;

import org.example.booking.models.Homestay;
import org.example.booking.models.Review;
import org.example.booking.services.HomestayService;
import org.example.booking.services.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ReviewService reviewService;
    private final HomestayService homestayService;

    @GetMapping("/")
    public String home(Model model) {
        // Lấy danh sách tất cả review từ cơ sở dữ liệu
        List<Review> reviews = reviewService.getAllReviews();
        // In ra số lượng review để debug
        System.out.println("Số review có sẵn: " + reviews.size());
        model.addAttribute("reviews", reviews);
        return "home"; // Trả về home.html
    }

    @GetMapping("/blog-single")
    public String blog() {
        return "blog-single";
    }

    @GetMapping("/search")
    public String searchHomestays(
            @RequestParam(value = "destination", required = false) String destination,
            @RequestParam(value = "priceLimit", required = false) Double priceLimit,
            Model model) {

        // Kiểm tra giá tối đa không âm
        if (priceLimit != null && priceLimit < 0) {
            model.addAttribute("error", "Price limit cannot be negative");
            return "search-results";
        }

        // Gọi service để tìm kiếm homestay
        List<Homestay> homestays = homestayService.searchHomestays(destination, priceLimit);

        // Thêm kết quả vào model để hiển thị
        model.addAttribute("homestays", homestays);
        model.addAttribute("destination", destination);
        model.addAttribute("priceLimit", priceLimit);

        // Trả về template kết quả tìm kiếm
        return "search-results";
    }
}