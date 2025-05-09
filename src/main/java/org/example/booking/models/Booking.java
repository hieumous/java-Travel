package org.example.booking.models;

import java.time.LocalDate;
import jakarta.persistence.*;
import org.example.booking.enums.BookingStatus;
import java.util.ArrayList;
import java.util.List;

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
    private boolean isPaid = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false)
    private int month;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "booking_services",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<ListFood> services = new ArrayList<>();

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
        this.month = checkInDate.getMonthValue();
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
        this.month = checkInDate.getMonthValue();
    }

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Homestay getHomestay() { return homestay; }
    public void setHomestay(Homestay homestay) { this.homestay = homestay; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        this.month = checkInDate.getMonthValue();
    }

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

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<ListFood> getServices() {
        return services;
    }

    public void setServices(List<ListFood> services) {
        this.services = services != null ? new ArrayList<>(services) : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
        if (this.checkInDate != null) {
            this.month = this.checkInDate.getMonthValue();
        }
    }
}