package org.example.booking.controllers;

import org.example.booking.models.Review;
import org.example.booking.services.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
//    @GetMapping("/home")
//    public String HomePage() {
//        return "home";
//    }


    private final ReviewService reviewService;
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

}
