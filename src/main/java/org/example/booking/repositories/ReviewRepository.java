package org.example.booking.repositories;

import org.example.booking.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Lấy tất cả đánh giá của một homestay theo homestayId
    List<Review> findByHomestayId(Long homestayId);

    // Lấy các đánh giá có số sao cụ thể (ví dụ: 5 sao, 4 sao)
    List<Review> findByRating(int rating);

    // Đếm số lượng đánh giá cho một homestay (để tính điểm trung bình, v.v.)
    Long countByHomestayId(Long homestayId);
}
