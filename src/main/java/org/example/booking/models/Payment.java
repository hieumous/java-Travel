package org.example.booking.models;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "payments")
public abstract class Payment {

    @Id
    private String id;

    private String transactionId;
    private double amount;
    private String currency;
    private String paymentMethod;
    private String paymentReference;

    @Enumerated(EnumType.STRING)
    private PaymentStatusType status;

    private String errorMessage;

    @ManyToOne
    @JoinColumn(name = "user_id") // Cột khóa ngoại trong bảng payments
    private User user; // Thêm trường user để liên kết với User

    public enum PaymentStatusType {
        PENDING,
        SUCCESSFUL,
        FAILED
    }

    // Constructor mặc định
    public Payment() {
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public PaymentStatusType getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusType status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}