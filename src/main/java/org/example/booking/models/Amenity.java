package org.example.booking.models;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "amenities")
    private List<Homestay> homestays;

    // Constructor không đối số
    public Amenity() {}

    // Constructor có đối số
    public Amenity(String name) {
        this.name = name;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Homestay> getHomestays() {
        return homestays;
    }

    public void setHomestays(List<Homestay> homestays) {
        this.homestays = homestays;
    }
}
