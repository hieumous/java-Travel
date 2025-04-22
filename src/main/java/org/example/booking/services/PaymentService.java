package org.example.booking.services;

import org.example.booking.models.PaymentInfo;
import org.example.booking.models.PaymentStatus;
import org.example.booking.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentStatus processPayment(PaymentInfo paymentInfo) {
        String transactionId = UUID.randomUUID().toString();

        // Tạo PaymentStatus
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(UUID.randomUUID().toString());
        paymentStatus.setTransactionId(transactionId);
        paymentStatus.setAmount(paymentInfo.getAmount());
        paymentStatus.setCurrency(paymentInfo.getCurrency());
        paymentStatus.setPaymentMethod(paymentInfo.getPaymentMethod().toString());
        paymentStatus.setPaymentReference(paymentInfo.getPaymentReference());
        paymentStatus.setUser(paymentInfo.getUser());
        paymentStatus.setStatus(PaymentStatus.PaymentStatusType.PENDING);

        // Giả lập kết quả thanh toán
        boolean paymentSuccessful = simulatePayment(paymentInfo.getPaymentMethod().toString());
        if (paymentSuccessful) {
            paymentStatus.setStatus(PaymentStatus.PaymentStatusType.SUCCESSFUL);
            paymentStatus.setPaymentGatewayId("SIMULATED_GATEWAY");
            paymentStatus.setPaymentGatewayTransactionId("SIM_TX_" + transactionId);
            paymentStatus.setPaymentGatewayResponseCode("00");
            paymentStatus.setPaymentGatewayResponseMessage("Payment simulated successfully");
        } else {
            paymentStatus.setStatus(PaymentStatus.PaymentStatusType.FAILED);
            paymentStatus.setErrorMessage("Simulated payment failed");
            paymentStatus.setPaymentGatewayResponseCode("99");
            paymentStatus.setPaymentGatewayResponseMessage("Payment simulation failed");
        }

        // Lưu PaymentStatus
        paymentRepository.save(paymentStatus);
        return paymentStatus;
    }

    // Phương thức giả lập thanh toán với tỷ lệ thành công/thất bại
    private boolean simulatePayment(String paymentMethod) {
        double successRate;
        if ("MOMO".equals(paymentMethod)) {
            successRate = 0.7; // 70% thành công cho MoMo
        } else if ("VNPAY".equals(paymentMethod)) {
            successRate = 0.7; // 70% thành công cho VNPay
        } else {
            successRate = 0.5;
        }
        return Math.random() < successRate;
    }
}