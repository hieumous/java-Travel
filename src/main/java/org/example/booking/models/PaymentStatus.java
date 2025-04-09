package org.example.booking.models;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_status")
public class PaymentStatus extends Payment {

    // Biến tĩnh SUCCESSFUL (khởi tạo nếu cần, nhưng không lưu vào DB)
    public static final PaymentStatus SUCCESSFUL = new PaymentStatus("gatewayId", "transId", "00", "Success");

    private String paymentGatewayId;
    private String paymentGatewayTransactionId;
    private String paymentGatewayResponseCode;
    private String paymentGatewayResponseMessage;

    // Mối quan hệ ngược lại với Order (nếu cần 2 chiều)
    @OneToOne(mappedBy = "paymentInfo")
    private Order order;

    // Constructor cho SUCCESSFUL (không lưu vào DB, chỉ dùng trong logic)
    public PaymentStatus(String paymentGatewayId, String paymentGatewayTransactionId,
                         String paymentGatewayResponseCode, String paymentGatewayResponseMessage) {
        this.paymentGatewayId = paymentGatewayId;
        this.paymentGatewayTransactionId = paymentGatewayTransactionId;
        this.paymentGatewayResponseCode = paymentGatewayResponseCode;
        this.paymentGatewayResponseMessage = paymentGatewayResponseMessage;
    }

    // Constructor mặc định (yêu cầu bởi JPA)
    public PaymentStatus() {
    }

    // Getter và Setter
    public String getPaymentGatewayId() {
        return paymentGatewayId;
    }

    public void setPaymentGatewayId(String paymentGatewayId) {
        this.paymentGatewayId = paymentGatewayId;
    }

    public String getPaymentGatewayTransactionId() {
        return paymentGatewayTransactionId;
    }

    public void setPaymentGatewayTransactionId(String paymentGatewayTransactionId) {
        this.paymentGatewayTransactionId = paymentGatewayTransactionId;
    }

    public String getPaymentGatewayResponseCode() {
        return paymentGatewayResponseCode;
    }

    public void setPaymentGatewayResponseCode(String paymentGatewayResponseCode) {
        this.paymentGatewayResponseCode = paymentGatewayResponseCode;
    }

    public String getPaymentGatewayResponseMessage() {
        return paymentGatewayResponseMessage;
    }

    public void setPaymentGatewayResponseMessage(String paymentGatewayResponseMessage) {
        this.paymentGatewayResponseMessage = paymentGatewayResponseMessage;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}