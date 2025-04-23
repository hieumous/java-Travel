package org.example.booking.controllers;

import org.example.booking.models.ListFood;  // Use ListFood instead of Food
import org.example.booking.repositories.FoodRepository;
import org.example.booking.enums.MenuItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class FoodMenu {

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/add-food")
    public String showAddFoodPage() {
        return "add-food";
    }

    @PostMapping("/save-food")
    public String saveFood(@RequestParam("nameFood") String name,
                           @RequestParam("description") String description,
                           @RequestParam("price") Double price,
                           @RequestParam("type") String type,
                           @RequestParam("image") MultipartFile imageFile,
                           Model model) {

        try {
            // Check if the image is too large
            if (imageFile.getSize() > 10485760) { // 10 MB limit (adjust as needed)
                model.addAttribute("message", "Image size is too large. Please upload an image smaller than 10MB.");
                return "add-food";
            }

            byte[] imageBytes = imageFile.getBytes();
            MenuItemType menuItemType = MenuItemType.valueOf(type.toUpperCase());

            ListFood food = new ListFood();
            food.setNameFood(name);
            food.setDescription(description);
            food.setPrice(price);
            food.setType(menuItemType);
            food.setImage(imageBytes);

            foodRepository.save(food);
            // ✅ Lưu vào bảng "foods"
            return "redirect:/ManageHomestays";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Có lỗi xảy ra khi tải ảnh.");
        }

        return "add-food";
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        Optional<ListFood> optionalFood = foodRepository.findById(id);
        if (optionalFood.isPresent() && optionalFood.get().getImage() != null) {
            byte[] image = optionalFood.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // hoặc IMAGE_PNG nếu ảnh là PNG
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/delete-food/{id}")
    public String deleteFood(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            foodRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa món ăn!");
        }
        return "redirect:/list-food";
    }
}