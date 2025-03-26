package org.example.booking.services;
import org.example.booking.exceptions.ResourceNotFoundException;
import org.example.booking.models.Order;
import org.example.booking.models.OrderRequest;
import org.example.booking.models.OrderResponse;
import org.example.booking.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.example.booking.models.Food;
import org.example.booking.repositories.FoodRepository;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    @Autowired
    private OrderRepository orderRepository;
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food createFood(Food food) {
        return foodRepository.save(food);
    }

    public Food updateFood(Long id, Food updatedFood) {
        Food existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

        existingFood.setName(updatedFood.getName());
        existingFood.setDescription(updatedFood.getDescription());
        existingFood.setPrice(updatedFood.getPrice());

        return foodRepository.save(existingFood);
    }

    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
    }

    public Iterable<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public OrderResponse orderFood(OrderRequest orderRequest) {
        // Kiểm tra dữ liệu đầu vào
        validateOrderRequest(orderRequest);

        // Tìm món ăn theo ID
        Food food = foodRepository.findById(orderRequest.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with ID: " + orderRequest.getFoodId()));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setFood(food);
        order.setQuantity(orderRequest.getQuantity());
        order.setDeliveryTime(orderRequest.getDeliveryTime());

        // Lưu đơn hàng vào OrderRepository
        Order savedOrder = orderRepository.save(order);

        // Trả về phản hồi đơn hàng
        return new OrderResponse(savedOrder.getId(), food.getName(), food.getPrice(), savedOrder.getQuantity(), savedOrder.getDeliveryTime());
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
