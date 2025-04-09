package org.example.booking.models;

import jakarta.persistence.*;
import org.example.booking.enums.PaymentMethod;

import java.util.List;

@Entity
@Table(name = "supplements")
public abstract class SupplementService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id") // Cột khóa ngoại trong bảng supplements
    private Order order; // Thêm trường để ánh xạ mối quan hệ ngược lại

    // Constructor mặc định (yêu cầu bởi JPA)
    public SupplementService() {
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // Phương thức trừu tượng
    public abstract Order createOrder(List<SupplementService> supplements, PaymentMethod paymentMethod);

    // Lớp lồng NewSupplementServiceRequest (giữ nguyên)
    public static class NewSupplementServiceRequest {
        private String name;
        private String description;
        private Double price;

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

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

    // Lớp lồng UpdateSupplementServiceRequest (giữ nguyên)
    public static class UpdateSupplementServiceRequest {
        private String name;
        private String description;
        private Double price;

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

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }
}