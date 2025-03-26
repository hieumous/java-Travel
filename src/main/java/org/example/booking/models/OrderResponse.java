package org.example.booking.models;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long orderId;
    private String itemName;
    private double itemPrice;
    private int quantity;
    private LocalDateTime deliveryTime;

    public OrderResponse(Long orderId, String itemName, double itemPrice, int quantity, LocalDateTime deliveryTime) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
        this.deliveryTime = deliveryTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }
}


