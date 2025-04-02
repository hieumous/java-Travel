package org.example.booking.controllers;

import org.example.booking.models.PaymentStatus;
import org.example.booking.models.PaymentInfo;
import org.example.booking.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * Initiates a payment.
     *
     * @param paymentInfo The payment information.
     * @return The PaymentStatus object.
     */
    @PostMapping("/initiate")
    @PreAuthorize("hasRole('USER')") // Chỉ người dùng đã đăng nhập mới có thể gọi API này
    public ResponseEntity<PaymentStatus> initiatePayment(@RequestBody PaymentInfo paymentInfo) {
        logger.info("Initiating payment with method: {}", paymentInfo.getPaymentMethod());

        try {
            PaymentStatus paymentStatus = paymentService.processPayment(paymentInfo);
            return new ResponseEntity<>(paymentStatus, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error initiating payment: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the payment status by transaction ID.
     *
     * @param transactionId The transaction ID of the payment.
     * @return The PaymentStatus object if found, or 404 if not found.
     */
    @GetMapping("/status/{transactionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentStatus> getPaymentStatus(@PathVariable String transactionId) {
        logger.info("Fetching payment status for transaction ID: {}", transactionId);

        // Note: Since PaymentRepository works with Payment, but PaymentService returns PaymentStatus,
        // we cannot directly query PaymentStatus. This is a design inconsistency.
        // For now, we assume the client will handle the status from the initiate call.
        // If you need to query PaymentStatus, you would need a PaymentStatusRepository.
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }
}