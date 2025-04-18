package org.example.booking.services;

import org.example.booking.models.Homestay;
import org.example.booking.enums.HomestayStatus;
import org.example.booking.repositories.HomestayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class HomestayService {
    @Autowired
    private HomestayRepository homestayRepository;

    @Autowired
    private ImageService imageService;

    public List<Homestay> findByName(String name) {
        return homestayRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Homestay> findByLocation(String location) {
        return homestayRepository.findByLocationIgnoreCase(location);
    }

    public List<Homestay> findByPrice(double minPrice, double maxPrice) {
        return homestayRepository.findByPricePerNightBetween(minPrice, maxPrice);
    }

    public Homestay registerHomestay(Homestay homestay, List<MultipartFile> images) {
        List<String> imageUrls = imageService.uploadImages(images);
        homestay.setImageUrls(imageUrls);
        homestay.setStatus(HomestayStatus.PENDING);
        return homestayRepository.save(homestay);
    }

    public List<Homestay> findAll() {
        return homestayRepository.findAll();
    }

    public Homestay findById(Long id) {
        return homestayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Homestay not found with id: " + id));
    }

    public Homestay save(Homestay homestay) {
        return homestayRepository.save(homestay);
    }

    public List<String> uploadImage(List<MultipartFile> files) {
        return imageService.uploadImages(files);
    }
}