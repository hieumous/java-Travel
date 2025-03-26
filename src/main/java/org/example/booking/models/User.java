package org.example.booking.models;
import jakarta.persistence.*;
import org.example.booking.enums.Role;
@Entity
@Table (name ="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String password,String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    public User() {}

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() { return role; }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {this.role = role;}
}
