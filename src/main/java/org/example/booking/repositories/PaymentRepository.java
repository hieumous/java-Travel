package org.example.booking.repositories;
import org.example.booking.models.Payment;
import org.example.booking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
    Optional<Payment> findByTransactionId(String transactionId);
}
