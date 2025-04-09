package org.example.booking.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "supplement_service_id")
    private SupplementService supplementService;

    private int quantity;

    private LocalDateTime deliveryTime;

    @OneToMany(mappedBy = "order")
    private List<SupplementService> items;

    private double totalAmount;

    @OneToOne // Mối quan hệ 1-1 với PaymentStatus
    @JoinColumn(name = "payment_status_id") // Cột khóa ngoại trong bảng orders
    private PaymentStatus paymentInfo;

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public SupplementService getSupplement() {
        return supplementService;
    }

    public void setSupplement(SupplementService supplement) {
        this.supplementService = supplement;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<SupplementService> getItems() {
        return items;
    }

    public void setItems(List<SupplementService> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentStatus paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}