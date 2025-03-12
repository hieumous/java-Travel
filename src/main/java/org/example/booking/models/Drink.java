package org.example.booking.models;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("DRINK")
public class Drink extends MenuItem {
    private String imageUrl; // Hình ảnh đồ uống

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
