package org.example.booking.services;

import org.example.booking.models.Drink;
import org.example.booking.repositories.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrinkService {
    private final DrinkRepository drinkRepository;

    @Autowired
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
}

