package org.example.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.booking.models.SupplementService;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SupplementServiceRepository extends JpaRepository<SupplementService, Long> {
    Optional<SupplementService> findByName(String name);
    Optional<SupplementService> findByDescription(String description);
}
