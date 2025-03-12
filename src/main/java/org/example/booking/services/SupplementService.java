package org.example.booking.services;
import jakarta.persistence.*;

@Entity
public class SupplementService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;

}
