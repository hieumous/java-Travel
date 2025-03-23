package org.example.booking.models;

import jakarta.persistence.*;
import org.example.booking.enums.WithdrawalStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawal_requests")
public class WithdrawalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Giả sử bạn có entity Homestay
    @ManyToOne
    @JoinColumn(name = "homestay_id")
    private Homestay homestay;

    private double amount;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status = WithdrawalStatus.PENDING;

    private LocalDateTime requestedAt = LocalDateTime.now();
    private LocalDateTime processedAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Homestay getHomestay() { return homestay; }
    public void setHomestay(Homestay homestay) { this.homestay = homestay; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public WithdrawalStatus getStatus() { return status; }
    public void setStatus(WithdrawalStatus status) { this.status = status; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
