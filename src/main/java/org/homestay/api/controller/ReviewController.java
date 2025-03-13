package org.homestay.api.controller;

import org.homestay.api.model.Review;
import org.homestay.api.model.ReviewResponse;
import org.homestay.api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // Tạo đánh giá (người dùng có ROLE_USER)
    @PostMapping("/{homestayId}")
    public ResponseEntity<Review> createReview(@PathVariable Long homestayId,
                                               @RequestParam int rating,
                                               @RequestParam String comment,
                                               @AuthenticationPrincipal UserDetails user) {
        // Ép kiểu sang đối tượng User của bạn nếu cần
        Long userId = ((org.homestay.api.model.User) user).getId();
        Review review = reviewService.createReview(homestayId, userId, rating, comment);
        return ResponseEntity.ok(review);
    }

    // Lấy danh sách đánh giá cho 1 homestay
    @GetMapping("/{homestayId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long homestayId) {
        List<Review> reviews = reviewService.getReviewsByHomestay(homestayId);
        return ResponseEntity.ok(reviews);
    }

    // Tạo phản hồi cho đánh giá (chủ homestay phản hồi)
    @PostMapping("/response/{reviewId}")
    public ResponseEntity<ReviewResponse> addResponse(@PathVariable Long reviewId,
                                                      @RequestParam String responseText) {
        ReviewResponse response = reviewService.createResponse(reviewId, responseText);
        return ResponseEntity.ok(response);
    }
}
