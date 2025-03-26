package org.example.booking.services;

import org.example.booking.exceptions.ResourceNotFoundException;
import org.example.booking.models.Order;
import org.example.booking.models.OrderRequest;
import org.example.booking.models.OrderResponse;
import org.example.booking.models.SupplementService;
import org.example.booking.repositories.OrderRepository;
import org.example.booking.repositories.SupplementServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplementServiceService {

    private final SupplementServiceRepository supplementServiceRepository;

    @Autowired
    private SupplementServiceRepository supplementRepository;
    private OrderRepository orderRepository;
    public SupplementServiceService(SupplementServiceRepository supplementServiceRepository) {
        this.supplementServiceRepository = supplementServiceRepository;
    }

    // Lấy tất cả các dịch vụ bổ sung
    public List<SupplementService> getAllSupplements() {
        return supplementServiceRepository.findAll();
    }

    // Lấy dịch vụ bổ sung theo ID
    public SupplementService getSupplementById(Long id) {
        return supplementServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplement not found with id: " + id));
    }

    // Tạo dịch vụ bổ sung mới
    public SupplementService createSupplement(SupplementService.NewSupplementServiceRequest request) {
        SupplementService supplementService = new SupplementService();
        supplementService.setName(request.getName());
        supplementService.setDescription(request.getDescription());
        supplementService.setPrice(request.getPrice());
        return supplementServiceRepository.save(supplementService);
    }

    // Cập nhật dịch vụ bổ sung
    public SupplementService updateSupplement(Long id, SupplementService.UpdateSupplementServiceRequest request) {
        SupplementService existingSupplement = getSupplementById(id);
        if (request.getName() != null) {
            existingSupplement.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingSupplement.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            existingSupplement.setPrice(request.getPrice());
        }
        return supplementServiceRepository.save(existingSupplement);
    }

    // Xóa dịch vụ bổ sung
    public void deleteSupplement(Long id) {
        supplementServiceRepository.deleteById(id);
    }

    public OrderResponse orderSupplement(OrderRequest orderRequest) {
        // Kiểm tra dữ liệu đầu vào
        validateOrderRequest(orderRequest);

        // Kiểm tra nếu Supplement ID bị null
        if (orderRequest.getSupplementId() == null) {
            throw new IllegalArgumentException("Supplement ID cannot be null");
        }

        // Tìm SupplementService theo ID
        SupplementService supplementService = supplementRepository.findById(orderRequest.getSupplementId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplement not found with ID: " + orderRequest.getSupplementId()));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setSupplement(supplementService);
        order.setQuantity(orderRequest.getQuantity());
        order.setDeliveryTime(orderRequest.getDeliveryTime());

        // Lưu đơn hàng vào OrderRepository
        Order savedOrder = orderRepository.save(order);

        // Trả về phản hồi đơn hàng
        return new OrderResponse(
                savedOrder.getId(),
                supplementService.getName(),
                supplementService.getPrice(),
                savedOrder.getQuantity(),
                savedOrder.getDeliveryTime()
        );
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        // Implement validation logic here
        if (orderRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number");
        }
        if (orderRequest.getDeliveryTime() == null) {
            throw new IllegalArgumentException("Delivery time is required");
        }
    }
}