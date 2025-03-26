package org.example.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.booking.models.Drink;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {
    Optional<Drink> findByName(String name);
}
