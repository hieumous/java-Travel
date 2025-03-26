package org.example.booking.controllers;
import org.example.booking.models.Homestay;
import org.example.booking.services.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homestays")
public class HomestayController {

    @Autowired
    private HomestayService homestayService;

    // Tim theo name
    @GetMapping("/search/name")
    public ResponseEntity<List<Homestay>> searchByName(@RequestParam("name") String name) {
        List<Homestay> results = homestayService.findByName(name);
        return ResponseEntity.ok(results);
    }

    // Tim theo location
    @GetMapping("/search/location")
    public ResponseEntity<List<Homestay>> searchByLocation(@RequestParam("location") String location) {
        List<Homestay> results = homestayService.findByLocation(location);
        return ResponseEntity.ok(results);
    }

    // Tim theo khoang gia
    @GetMapping("/search/price")
    public ResponseEntity<List<Homestay>> searchByPrice(@RequestParam("minPrice") double minPrice,
                                                        @RequestParam("maxPrice") double maxPrice) {
        List<Homestay> results = homestayService.findByPrice(minPrice, maxPrice);
        return ResponseEntity.ok(results);
    }
}
