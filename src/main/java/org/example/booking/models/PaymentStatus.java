package org.example.booking.models;

public class PaymentStatus extends Payment {
    private String paymentGatewayId;
    private String paymentGatewayTransactionId;
    private String paymentGatewayResponseCode;
    private String paymentGatewayResponseMessage;

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
}

