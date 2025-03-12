package org.example.booking.services;
import org.example.booking.models.User;
import org.example.booking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class UserService {
    //ko cần cấp phát lại toán tử new
    @Autowired
    private UserRepository userRepository;

    //tim kiem theo username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    //Tim kiem theo email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //save user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    //delete theo id
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User không tồn tại!");
        }
    }



}
