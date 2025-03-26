package org.example.booking.controllers;

import org.example.booking.models.Drink;
import org.example.booking.models.OrderRequest;
import org.example.booking.models.OrderResponse;
import org.example.booking.services.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drinks")
@Validated
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    /**
     * Lấy danh sách tất cả các đồ uống.
     *
     * @return Danh sách đồ uống
     */
    @GetMapping
    public ResponseEntity<List<Drink>> getAllDrinks() {
        List<Drink> drinks = drinkService.getAllDrinks();
        return ResponseEntity.ok(drinks);
    }

    /**
     * Lấy thông tin của một đồ uống cụ thể.
     *
     * @param id ID của đồ uống
     * @return Thông tin của đồ uống
     */
    @GetMapping("/{id}")
    public ResponseEntity<Drink> getDrinkById(Long id) {
        Drink drink = drinkService.getDrinkById(id);
        if (drink == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drink);
    }


    /**
     * Tạo mới một đồ uống.
     *
     * @param newDrink Thông tin đồ uống mới
     * @return Thông tin đồ uống đã được tạo
     */
    @PostMapping
    public ResponseEntity<Drink> createDrink(@RequestBody Drink.NewDrinkRequest newDrink) {
        Drink createdDrink = drinkService.createDrink(newDrink);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDrink);
    }

    /**
     * Cập nhật thông tin của một đồ uống.
     *
     * @param id       ID của đồ uống cần cập nhật
     * @param updatedDrink Thông tin đồ uống mới
     * @return Thông tin đồ uống đã được cập nhật
     */
    @PutMapping("/{id}")
    public ResponseEntity<Drink> updateDrink(@PathVariable Long id, @RequestBody Drink.UpdateDrinkRequest updatedDrink) {
        try {
            Drink updatedDrinkEntity = drinkService.updateDrink(id, updatedDrink);
            return ResponseEntity.ok(updatedDrinkEntity);
        } catch (IllegalArgumentException e) {
            // Xử lý trường hợp id không hợp lệ
            return ResponseEntity.badRequest().build();
        }
    }
    /**
     * Xóa một đồ uống.
     *
     * @param id ID của đồ uống cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDrink(@PathVariable  Long id) {
        drinkService.deleteDrink(id);
        return ResponseEntity.ok("Xóa đồ uống thành công!");
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> orderDrink(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = drinkService.orderDrink(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }
}
