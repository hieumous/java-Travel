package org.example.booking.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    public String convertToBase64(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                byte[] fileBytes = file.getBytes();
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileBytes);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert image to Base64: " + e.getMessage(), e);
        }
    }
}