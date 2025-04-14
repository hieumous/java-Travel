package org.example.booking.models;

import org.example.booking.enums.PaymentMethod;
import org.example.booking.models.Order;
import org.example.booking.models.User;
import java.time.LocalDateTime;

/**
 * Represents the information required to process a payment transaction.
 */
public class PaymentInfo {
    private Long id;
    private Order order;
    private User user;
    private PaymentMethod paymentMethod;
    private double amount;
    private String currency;
    private String transactionId;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentReference;
    private double totalAmount;
    private Long bookingId;
    private Long homestayId;


    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getHomestayId() {
        return homestayId;
    }

    public void setHomestayId(Long homestayId) {
        this.homestayId = homestayId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void processPayment() {
        // Implement the logic to process the payment
    }

    public void cancelPayment() {
        // Implement the logic to cancel the payment
    }

    public void refundPayment() {
        // Implement the logic to refund the payment
    }


    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public PaymentInfo getPaymentInfo() {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setTransactionId(this.transactionId);
        paymentInfo.setAmount(this.totalAmount);
        paymentInfo.setPaymentMethod(this.paymentMethod);
        // Add other relevant payment information
        return paymentInfo;
    }

}