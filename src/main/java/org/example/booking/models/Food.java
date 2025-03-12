package org.example.booking.models;
import jakarta.persistence.*;
@Entity
@DiscriminatorValue("FOOD")
public class Food extends MenuItem {
    private String imageUrl; // Hình ảnh món ăn

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
