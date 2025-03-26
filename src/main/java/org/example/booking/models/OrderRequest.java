package org.example.booking.models;

import java.time.LocalDateTime;

public class OrderRequest {
    private Long itemId;
    private int quantity;
    private LocalDateTime deliveryTime;
    private Long foodId;
    private Long drinkId;
    private Long supplementId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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


    public Long getFoodId() {
        return foodId;
    }

    public Long getDrinkId() {
        return drinkId;
    }

    public Long getSupplementId() {
        return supplementId;
    }

    public void setSupplementId(Long supplementId) {
        this.supplementId = supplementId;
    }

}

