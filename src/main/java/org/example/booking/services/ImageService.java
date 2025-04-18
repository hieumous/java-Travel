package org.example.booking.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public List<String> uploadImages(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR, fileName);
                    Files.write(filePath, file.getBytes());
                    imageUrls.add("/uploads/" + fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrls;
    }
}