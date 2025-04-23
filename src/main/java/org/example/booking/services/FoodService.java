package org.example.booking.services;

import org.example.booking.exceptions.ResourceNotFoundException;
import org.example.booking.models.ListFood; // Use ListFood instead of Food
import org.example.booking.models.Order;
import org.example.booking.models.OrderRequest;
import org.example.booking.models.OrderResponse;
import org.example.booking.repositories.FoodRepository;
import org.example.booking.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;

    // Constructor injection for both FoodRepository and OrderRepository
    @Autowired
    public FoodService(FoodRepository foodRepository, OrderRepository orderRepository) {
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
    }

    // Create a new food item
    public ListFood createFood(ListFood food) {  // Use ListFood instead of Food
        return foodRepository.save(food);
    }

    // Update an existing food item
    public ListFood updateFood(Long id, ListFood updatedFood) {  // Use ListFood instead of Food
        ListFood existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));

        existingFood.setNameFood(updatedFood.getNameFood());
        existingFood.setDescription(updatedFood.getDescription());
        existingFood.setPrice(updatedFood.getPrice());

        return foodRepository.save(existingFood);
    }

    // Delete a food item by ID
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    // Retrieve a food item by ID
    public ListFood getFoodById(Long id) {  // Use ListFood instead of Food
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));
    }

    // Get all food items
    public Iterable<ListFood> getAllFoods() {  // Use ListFood instead of Food
        return foodRepository.findAll();
    }

    // Place an order for food
    @Transactional
    public OrderResponse orderFood(OrderRequest orderRequest) {
        // Validate the order request data
        validateOrderRequest(orderRequest);

        // Find the food item by ID
        ListFood food = foodRepository.findById(orderRequest.getFoodId())  // Use ListFood instead of Food
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with ID: " + orderRequest.getFoodId()));

        // Calculate total price (Optional, if needed)
        double totalPrice = food.getPrice() * orderRequest.getQuantity();

        // Create a new order
        Order order = new Order();
        order.setQuantity(orderRequest.getQuantity());
        order.setDeliveryTime(orderRequest.getDeliveryTime());

        // Save the order in the repository
        Order savedOrder = orderRepository.save(order);

        // Return order response with additional info
        return new OrderResponse(savedOrder.getId(), food.getNameFood(), totalPrice, savedOrder.getQuantity(), savedOrder.getDeliveryTime());
    }

    // Validate the incoming order request
    private void validateOrderRequest(OrderRequest orderRequest) {
        if (orderRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number");
        }
        if (orderRequest.getDeliveryTime() == null) {
            throw new IllegalArgumentException("Delivery time is required");
        }
    }
}
