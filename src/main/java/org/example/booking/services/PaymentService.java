package org.example.booking.services;

import org.example.booking.models.Payment;
import org.example.booking.models.PaymentInfo;
import org.example.booking.models.PaymentStatus;
import org.example.booking.repositories.PaymentRepository;

import java.util.UUID;

public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Process a payment using the provided payment information.
     *
     * @param paymentInfo The payment information.
     * @return The payment status.
     */
    public PaymentStatus processPayment(PaymentInfo paymentInfo) {
        // Generate a unique transaction ID
        String transactionId = UUID.randomUUID().toString();

        // Create a new payment status
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(UUID.randomUUID().toString());
        paymentStatus.setTransactionId(transactionId);
        paymentStatus.setAmount(paymentInfo.getAmount());
        paymentStatus.setCurrency(paymentInfo.getCurrency());
        paymentStatus.setPaymentMethod(paymentInfo.getPaymentMethod().name());
        paymentStatus.setPaymentReference(paymentInfo.getPaymentReference());
        paymentStatus.setStatus(PaymentStatus.PaymentStatusType.PENDING);

        // Attempt to process the payment
        try {
            // Call the payment gateway to process the payment
            boolean paymentSuccessful = processPaymentWithGateway(paymentInfo);

            // Update the payment status based on the result
            if (paymentSuccessful) {
                paymentStatus.setStatus(PaymentStatus.PaymentStatusType.SUCCESSFUL);
            } else {
                paymentStatus.setStatus(PaymentStatus.PaymentStatusType.FAILED);
                paymentStatus.setErrorMessage("Payment processing failed.");
            }
        } catch (Exception e) {
            // Handle any exceptions that occurred during payment processing
            paymentStatus.setStatus(PaymentStatus.PaymentStatusType.FAILED);
            paymentStatus.setErrorMessage(e.getMessage());
        }

        // Save the payment status to the repository
        paymentRepository.save(paymentStatus);

        return paymentStatus;
    }


    /**
     * Processes the payment using the payment gateway.
     *
     * @param paymentInfo The payment information.
     * @return true if the payment was successful, false otherwise.
     */
    private boolean processPaymentWithGateway(PaymentInfo paymentInfo) {
        // Call the payment gateway API to process the payment
        // and return the result
        return true; // Placeholder for actual payment processing logic
    }
}