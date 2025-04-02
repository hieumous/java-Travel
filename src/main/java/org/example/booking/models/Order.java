package org.example.booking.models;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Drink drink;
    private Food food;
    private SupplementService supplementService;
    private int quantity;
    private LocalDateTime deliveryTime;

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
}