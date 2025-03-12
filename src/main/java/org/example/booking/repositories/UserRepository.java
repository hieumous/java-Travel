package org.example.booking.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.booking.models.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Trả về Optional để tránh null khi tìm kiếm user theo username
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


}
