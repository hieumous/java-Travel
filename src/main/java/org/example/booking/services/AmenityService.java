package org.example.booking.services;

import org.example.booking.models.Amenity;
import org.example.booking.repositories.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmenityService {

    @Autowired
    private AmenityRepository amenityRepository;

    public void save(Amenity amenity) {
        amenityRepository.save(amenity); // Lưu homestay vào cơ sở dữ liệu
    }

    public List<Amenity> findAll() {
        return amenityRepository.findAll(); // Lấy tất cả homestay
    }

    public Amenity findById(Long id) {
        return amenityRepository.findById(id).orElse(null); // Tìm homestay theo ID
    }

    public void deleteById(Long id) {
        amenityRepository.deleteById(id); // Xóa homestay theo ID
    }
}