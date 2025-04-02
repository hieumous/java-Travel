package org.example.booking.services;
import org.example.booking.models.Homestay;
import org.example.booking.enums.HomestayStatus;
import org.example.booking.repositories.HomestayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
@Service
public class HomestayService {
    //ko cần cấp phát lại toán tử new
    @Autowired
    private HomestayRepository homestayRepository;
    @Autowired
    private ImageService ImageService;
    //Tim kiem theo name
    public List<Homestay> findByName(String name) {
        return homestayRepository.findByNameContainingIgnoreCase(name);
    }
    //Tim kiem theo location
    public List<Homestay> findByLocation(String location) {
        return homestayRepository.findByLocationIgnoreCase(location);
    }
    //Tim theo khoang gia
    public List<Homestay> findByPrice(double minPrice,double maxPrice) {
        return homestayRepository.findByPricePerNightBetween(minPrice, maxPrice);
    }
    public Homestay registerHomestay(Homestay homestay, List<MultipartFile> images) {
        // Upload ảnh
        List<String> imageUrls = ImageService.uploadImages(images);
        homestay.setImageUrls(imageUrls);

        // Lưu homestay
        homestay.setStatus(HomestayStatus.PENDING); // Đợi duyệt
        return homestayRepository.save(homestay);
    }

}
