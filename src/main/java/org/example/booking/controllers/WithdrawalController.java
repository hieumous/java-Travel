package org.example.booking.controllers;

import org.example.booking.models.WithdrawalRequest;
import org.example.booking.services.WithdrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/withdrawals")
@RequiredArgsConstructor
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    // Tạo yêu cầu rút tiền
    @PostMapping("/{homestayId}")
    public ResponseEntity<WithdrawalRequest> requestWithdrawal(@PathVariable Long homestayId,
                                                                   @RequestParam double amount) {
        WithdrawalRequest request = withdrawalService.createWithdrawalRequest(homestayId, amount);
        return ResponseEntity.ok(request);
    }

    // Xử lý yêu cầu rút tiền (Admin hoặc xử lý tự động)
    @PutMapping("/{requestId}/process")
    public ResponseEntity<WithdrawalRequest> processWithdrawal(@PathVariable Long requestId) {
        WithdrawalRequest processedRequest = withdrawalService.processWithdrawal(requestId);
        return ResponseEntity.ok(processedRequest);
    }
}