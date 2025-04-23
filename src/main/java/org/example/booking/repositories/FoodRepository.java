package org.example.booking.repositories;

import org.example.booking.models.ListFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<ListFood, Long> {
    // Custom query methods if needed
}