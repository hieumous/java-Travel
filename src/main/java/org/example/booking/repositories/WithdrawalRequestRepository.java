package org.example.booking.repositories;


import org.example.booking.models.WithdrawalRequest;
import org.example.booking.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Long> {

    // Lấy danh sách yêu cầu rút tiền theo trạng thái
    List<WithdrawalRequest> findByStatus(WithdrawalStatus status);

    // Lấy yêu cầu rút tiền theo homestayId
    List<WithdrawalRequest> findByHomestayId(Long homestayId);
}
