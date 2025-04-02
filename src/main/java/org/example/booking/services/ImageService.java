package org.example.booking.services;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    public List<String> uploadImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            // Ở đây có thể là logic upload lên server, Cloud Storage (S3, Firebase, v.v.)
            String imageUrl = "https://example.com/uploads/" + image.getOriginalFilename();
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }
}