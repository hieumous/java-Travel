package org.example.booking.repositories;

import org.example.booking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo tên đăng nhập
    Optional<User> findByUsername(String username);

    // Tìm user theo email (nếu cần, không bắt buộc dùng)
    Optional<User> findByEmail(String email);


}

