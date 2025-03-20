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

    public static class NewDrinkRequest {
        private String name;
        private String description;
        private double price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    public static class UpdateDrinkRequest {
        private String name;
        private double price;
        private String description;

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
