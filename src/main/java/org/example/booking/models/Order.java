package org.example.booking.models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Drink drink;
    private Food food;
    private SupplementService supplementService;
    private int quantity;
    private LocalDateTime deliveryTime;
    private List<SupplementService> items;
    private double totalAmount;
    private PaymentStatus paymentInfo;

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

    public void setItems(List<SupplementService> items) {
        this.items = items;
    }

    public List<SupplementService> getItems() {
        return items;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = (double) totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setPaymentInfo(PaymentStatus paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public PaymentStatus getPaymentInfo() {
        return paymentInfo;
    }

}