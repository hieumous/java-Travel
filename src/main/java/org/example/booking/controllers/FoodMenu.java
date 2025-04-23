package org.example.booking.controllers;

import org.example.booking.models.Homestay;
import org.example.booking.models.ListFood;
import org.example.booking.repositories.FoodRepository;
import org.example.booking.repositories.HomestayRepository;
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
import java.util.Optional;

@Controller
public class FoodMenu {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private HomestayRepository homestayRepository;

    @GetMapping("/add-food")
    public String showAddFoodPage(@RequestParam(value = "homestayId", required = false) Long homestayId, Model model) {
        if (homestayId != null) {
            Optional<Homestay> homestay = homestayRepository.findById(homestayId);
            if (homestay.isPresent()) {
                model.addAttribute("homestay", homestay.get());
            } else {
                model.addAttribute("error", "Homestay không tồn tại.");
            }
        }
        model.addAttribute("food", new ListFood());
        return "add-food";
    }

    @PostMapping("/save-food")
    public String saveFood(@RequestParam("nameFood") String name,
                           @RequestParam("description") String description,
                           @RequestParam("price") Double price,
                           @RequestParam("type") String type,
                           @RequestParam("image") MultipartFile imageFile,
                           @RequestParam(value = "homestayId", required = false) Long homestayId,
                           Model model) {

        try {
            if (imageFile.getSize() > 10485760) {
                model.addAttribute("message", "Ảnh quá lớn. Vui lòng tải ảnh nhỏ hơn 10MB.");
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

            if (homestayId != null) {
                Optional<Homestay> homestay = homestayRepository.findById(homestayId);
                if (homestay.isPresent()) {
                    food.setHomestay(homestay.get());
                } else {
                    model.addAttribute("message", "Homestay không tồn tại.");
                    return "add-food";
                }
            }

            foodRepository.save(food);
            return "redirect:/list-food" + (homestayId != null ? "?homestayId=" + homestayId : "");
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
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/delete-food/{id}")
    public String deleteFood(@PathVariable Long id, @RequestParam(value = "homestayId", required = false) Long homestayId, RedirectAttributes redirectAttributes) {
        try {
            foodRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa món ăn!");
        }
        return "redirect:/list-food" + (homestayId != null ? "?homestayId=" + homestayId : "");
    }
}