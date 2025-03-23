package org.example.booking.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "revenue_reports")
public class RevenueReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "homestay_id")
    private Homestay homestay;

    private double totalIncome;
    private int numberOfBookings;
    private double occupancyRate;
    private double serviceFee;
    private LocalDate month;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Homestay getHomestay() { return homestay; }
    public void setHomestay(Homestay homestay) { this.homestay = homestay; }

    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }

    public int getNumberOfBookings() { return numberOfBookings; }
    public void setNumberOfBookings(int numberOfBookings) { this.numberOfBookings = numberOfBookings; }

    public double getOccupancyRate() { return occupancyRate; }
    public void setOccupancyRate(double occupancyRate) { this.occupancyRate = occupancyRate; }

    public double getServiceFee() { return serviceFee; }
    public void setServiceFee(double serviceFee) { this.serviceFee = serviceFee; }

    public LocalDate getMonth() { return month; }
    public void setMonth(LocalDate month) { this.month = month; }
}

