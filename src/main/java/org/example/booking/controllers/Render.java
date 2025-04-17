package org.example.booking.controllers;

import org.example.booking.models.Amenity;
import org.example.booking.services.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/homestays")
public class Render {

    @Autowired
    private AmenityService amenityService;

    // Hiển thị danh sách
    @GetMapping
    public String listHomestays(Model model) {
        List<Amenity> homestays = amenityService.findAll();
        model.addAttribute("homestays", homestays);
        return "home"; // trả về file home.html
    }

    // Form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("homestay", new Amenity());
        return "add-homestay"; // form bạn đã thiết kế
    }

    // Xử lý lưu homestay và ảnh vào database
    @PostMapping("/save")
    public String saveHomestay(@ModelAttribute Amenity amenity,
                               @RequestParam("image") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Lưu nội dung ảnh vào cột imageData trong DB
                byte[] imageBytes = file.getBytes();
                amenity.setImageData(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Lưu thông tin homestay vào cơ sở dữ liệu
        amenityService.save(amenity);

        return "redirect:/homestays"; // Trả về đúng route danh sách
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        Amenity amenity = amenityService.findById(id);
        return amenity.getImageData();
    }
}
