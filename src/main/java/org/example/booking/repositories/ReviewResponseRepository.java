package org.example.booking.repositories;


import org.example.booking.models.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReviewResponseRepository extends JpaRepository<ReviewResponse, Long> {

    // Tìm phản hồi dựa trên reviewId
    Optional<ReviewResponse> findByReviewId(Long reviewId);
}
