package org.example.booking.models;

import jakarta.persistence.*;
import org.example.booking.enums.MenuItemType;

@Entity
@DiscriminatorValue("FOOD")
public class Food extends MenuItem {

    @Enumerated(EnumType.STRING) // Đảm bảo rằng enum sẽ được lưu dưới dạng chuỗi
    @Column(name = "type", nullable = false)
    private MenuItemType type; // Loại món ăn (FOOD hay DRINK)

    @Column(name = "name_food", nullable = false) // Đảm bảo rằng tên món ăn là bắt buộc
    private String nameFood; // Tên món ăn

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Mô tả món ăn

    @Column(name = "price")
    private Double price; // Giá món ăn

//    @Column(name = "image_url") // Nếu bạn muốn lưu URL ảnh
//    private String imageUrl;

//    @Lob // Đánh dấu là dữ liệu nhị phân lớn (binary large object)
//    @Column(name = "image", columnDefinition = "BLOB") // Lưu ảnh dưới dạng binary
//    private byte[] image;

    // ======= Getters and Setters =======
    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImage(byte[] imageBytes) {
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }

//    public byte[] getImage() {
//        return image;
//    }
//
//    public void setImage(byte[] image) {
//        this.image = image;
//    }

    // Các constructor khác nếu cần
}
