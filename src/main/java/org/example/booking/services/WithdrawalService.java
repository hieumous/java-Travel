package org.example.booking.services;

import org.example.booking.models.WithdrawalRequest;
import org.example.booking.enums.WithdrawalStatus;
import org.example.booking.models.Homestay;
import org.example.booking.repositories.WithdrawalRequestRepository;
import org.example.booking.repositories.HomestayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawalService {
    private final WithdrawalRequestRepository withdrawalRequestRepository;
    private final HomestayRepository homestayRepository;

    public WithdrawalRequest createWithdrawalRequest(Long homestayId, double amount) {
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new RuntimeException("Homestay không tồn tại"));
        // Kiểm tra số dư (logic tùy theo nghiệp vụ)
        boolean sufficientBalance = true;
        if (!sufficientBalance) {
            throw new RuntimeException("Số dư không đủ để rút");
        }
        WithdrawalRequest request = new WithdrawalRequest();
        request.setHomestay(homestay);
        request.setAmount(amount);
        return withdrawalRequestRepository.save(request);
    }

    public WithdrawalRequest processWithdrawal(Long requestId) {
        WithdrawalRequest request = withdrawalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Yêu cầu không tồn tại"));
        // Giả lập tích hợp API thanh toán bên thứ ba
        boolean success = true;
        if (success) {
            request.setStatus(WithdrawalStatus.APPROVED);
            request.setProcessedAt(LocalDateTime.now());
        } else {
            request.setStatus(WithdrawalStatus.REJECTED);
        }
        return withdrawalRequestRepository.save(request);
    }
}
