package org.example.booking.services;


import org.example.booking.models.Review;
import org.example.booking.models.ReviewResponse;
import org.example.booking.models.Homestay;
import org.example.booking.models.User;
import org.example.booking.repositories.ReviewRepository;
import org.example.booking.repositories.ReviewResponseRepository;
import org.example.booking.repositories.HomestayRepository;
import org.example.booking.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewResponseRepository reviewResponseRepository;
    private final HomestayRepository homestayRepository;
    private final UserRepository userRepository;

    public Review createReview(Long homestayId, Long userId, int rating, String comment) {
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new RuntimeException("Homestay không tồn tại"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Review review = new Review();
        review.setHomestay(homestay);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByHomestay(Long homestayId) {
        return reviewRepository.findByHomestayId(homestayId);
    }

    public ReviewResponse createResponse(Long reviewId, String responseText) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review không tồn tại"));
        ReviewResponse response = new ReviewResponse();
        response.setReview(review);
        response.setResponseText(responseText);
        return reviewResponseRepository.save(response);
    }
}
