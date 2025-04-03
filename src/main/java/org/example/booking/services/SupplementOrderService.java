package org.example.booking.services;

import org.example.booking.enums.PaymentMethod;
import org.example.booking.models.Order;
import org.example.booking.models.PaymentInfo;
import org.example.booking.models.PaymentStatus;
import org.example.booking.models.SupplementService;
import org.example.booking.repositories.OrderRepository;
import org.example.booking.repositories.SupplementServiceRepository;

import java.util.List;

public class SupplementOrderService extends SupplementService {
    private final SupplementServiceRepository supplementRepository;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    public SupplementOrderService(SupplementServiceRepository supplementRepository,
                                  PaymentService paymentService,
                                  OrderRepository orderRepository) {
        this.supplementRepository = supplementRepository;
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(List<SupplementService> supplements, PaymentMethod paymentMethod) {
        Order order = new Order();
        order.setItems(supplements);
        order.setTotalAmount(calculateTotalAmount(supplements));

        PaymentStatus paymentInfo = paymentService.processPayment(order.getPaymentInfo());
        order.setPaymentInfo(paymentInfo);
        return order;

    }

    private double calculateTotalAmount(List<SupplementService> supplements) {
        double totalAmount = 0;
        for (SupplementService supplement : supplements) {
            totalAmount += supplement.getPrice();
        }
        return totalAmount;
    }


    // Other SupplementService methods
}
