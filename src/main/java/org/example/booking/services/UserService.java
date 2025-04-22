
package org.example.booking.services;

import org.example.booking.models.User;
import org.example.booking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Đăng ký người dùng mới
    public void registerUser(User user) {
        userRepository.save(user); // Không mã hóa mật khẩu
    }

    // Lưu hoặc cập nhật người dùng
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Tìm người dùng theo username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Tìm người dùng theo email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Xóa người dùng theo ID
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Người dùng không tồn tại!");
        }
    }
}
