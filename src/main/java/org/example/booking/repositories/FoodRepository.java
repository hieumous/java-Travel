package org.example.booking.repositories;

import org.example.booking.models.ListFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<ListFood, Long> {
    List<ListFood> findByHomestayId(Long homestayId);
}