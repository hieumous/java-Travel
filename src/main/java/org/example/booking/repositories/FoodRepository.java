package org.example.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.booking.models.Food;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByName(String name);
    Optional<Food> findByDescription(String description);
}
