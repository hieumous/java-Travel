package org.example.booking.services;

import org.example.booking.exceptions.ResourceNotFoundException;
import org.example.booking.models.Drink;
import org.example.booking.models.Order;
import org.example.booking.models.OrderRequest;
import org.example.booking.models.OrderResponse;
import org.example.booking.repositories.DrinkRepository;
import org.example.booking.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrinkService {
    private final DrinkRepository drinkRepository;

    @Autowired
    private OrderRepository orderRepository;
    public DrinkService(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    public List<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    public Drink getDrinkById(Long id) {
        return drinkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drink not found with id: " + id));
    }

    public Drink createDrink(Drink.NewDrinkRequest newDrinkRequest) {
        Drink drink = new Drink();
        drink.setName(newDrinkRequest.getName());
        drink.setDescription(newDrinkRequest.getDescription());
        drink.setPrice(newDrinkRequest.getPrice());
        // Set thêm các thuộc tính khác nếu cần
        return drinkRepository.save(drink);
    }

    public Drink updateDrink(Long id, Drink.UpdateDrinkRequest updatedDrink) {
        Drink drink = getDrinkById(id);
        drink.setName(updatedDrink.getName());
        drink.setPrice(updatedDrink.getPrice());
        drink.setDescription(updatedDrink.getDescription());
        return drinkRepository.save(drink);
    }

    public void deleteDrink(Long id) {
        drinkRepository.deleteById(id);
    }

    public OrderResponse orderDrink(OrderRequest orderRequest) {
        // Kiểm tra dữ liệu đầu vào
        validateOrderRequest(orderRequest);

        // Kiểm tra nếu Drink ID bị null
        if (orderRequest.getDrinkId() == null) {
            throw new IllegalArgumentException("Drink ID cannot be null");
        }

        // Tìm đồ uống theo ID
        Drink drink = drinkRepository.findById(orderRequest.getDrinkId())
                .orElseThrow(() -> new ResourceNotFoundException("Drink not found with ID: " + orderRequest.getDrinkId()));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setDrink(drink);
        order.setQuantity(orderRequest.getQuantity());
        order.setDeliveryTime(orderRequest.getDeliveryTime());

        // Lưu đơn hàng vào OrderRepository
        Order savedOrder = orderRepository.save(order);

        // Trả về phản hồi đơn hàng
        return new OrderResponse(savedOrder.getId(), drink.getName(), drink.getPrice(), savedOrder.getQuantity(), savedOrder.getDeliveryTime());
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

