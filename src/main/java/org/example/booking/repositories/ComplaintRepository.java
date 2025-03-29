package org.example.booking.repositories;


import org.example.booking.models.Complaint;
import org.example.booking.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Lấy danh sách khiếu nại theo trạng thái (ví dụ: NEW, RESOLVED,...)
    List<Complaint> findByStatus(ComplaintStatus status);

    // Lấy danh sách khiếu nại của một user theo userId
    List<Complaint> findByUserId(Long userId);
}
