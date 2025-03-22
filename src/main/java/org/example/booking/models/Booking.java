package org.example.booking.models;

import java.time.LocalDate;
import jakarta.persistence.*;
import org.example.booking.enums.BookingStatus;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "homestay_id", nullable = false)
    private Homestay homestay;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private boolean isPaid = false;  // Mặc định là chưa thanh toán

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    // Constructor không đối số
    public Booking() {}

    // Constructor có đối số (mặc định status là PENDING)
    public Booking(Homestay homestay, User user, LocalDate checkInDate, LocalDate checkOutDate, boolean isPaid) {
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        this.homestay = homestay;
        this.user = user;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isPaid = isPaid;
    }

    // Constructor có trạng thái tùy chỉnh
    public Booking(Homestay homestay, User user, LocalDate checkInDate, LocalDate checkOutDate, boolean isPaid, BookingStatus status) {
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        this.homestay = homestay;
        this.user = user;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isPaid = isPaid;
        this.status = status != null ? status : BookingStatus.PENDING;
    }

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Homestay getHomestay() { return homestay; }
    public void setHomestay(Homestay homestay) { this.homestay = homestay; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) {
        if (checkOutDate.isBefore(this.checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        this.checkOutDate = checkOutDate;
    }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean isPaid) { this.isPaid = isPaid; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public int getBookingMonth() { return checkInDate.getMonthValue(); }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
    }
}
