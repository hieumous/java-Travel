package org.example.booking.controllers;

import org.example.booking.models.Homestay;
import org.example.booking.models.ListFood;
import org.example.booking.repositories.FoodRepository;
import org.example.booking.services.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class Render {

    @Autowired
    private HomestayService homestayService;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public String listHomestays(Model model) {
        List<Homestay> homestays = homestayService.findAll();
        // Tạo map lưu số lượng dịch vụ cho mỗi homestay
        Map<Long, Integer> serviceCounts = new HashMap<>();
        homestays.forEach(h -> serviceCounts.put(h.getId(), foodRepository.findByHomestayId(h.getId()).size()));
        model.addAttribute("homestays", homestays);
        model.addAttribute("serviceCounts", serviceCounts);
        return "home";
    }

    @GetMapping("/homestay/{id}")
    public String showHomestayDetail(@PathVariable Long id, Model model) {
        Homestay homestay = homestayService.findById(id);
        if (homestay == null) {
            model.addAttribute("homestay", null);
        } else {
            model.addAttribute("homestay", homestay);
            List<ListFood> services = foodRepository.findByHomestayId(id);
            model.addAttribute("services", services);
        }
        return "homestay-detail";
    }

    @GetMapping("/home/bookings/{id}")
    public String viewBookings(@PathVariable("id") Long id, Model model) {
        Homestay homestay = homestayService.findById(id);
        model.addAttribute("homestay", homestay);
        return "list-room";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("homestay", new Homestay());
        return "add-homestay";
    }

    @PostMapping("/save")
    public String saveHomestay(@ModelAttribute Homestay homestay,
                               @RequestParam("imageFile") MultipartFile file) {
        homestayService.registerHomestay(homestay, file);
        return "redirect:/ManageHomestays";
    }

    @PostMapping("/delete/{id}")
    public String deleteHomestay(@PathVariable Long id) {
        homestayService.deleteById(id);
        return "redirect:/ManageHomestays";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Homestay homestay = homestayService.findById(id);
        model.addAttribute("homestay", homestay);
        return "add-homestay";
    }

    @GetMapping("/service/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getServiceImage(@PathVariable Long id) {
        ListFood service = foodRepository.findById(id).orElse(null);
        if (service == null || service.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(service.getImage());
    }

    @GetMapping("/homestay/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getHomestayImage(@PathVariable Long id) {
        Homestay homestay = homestayService.findById(id);
        if (homestay == null || homestay.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(homestay.getImage().getBytes());
    }
}