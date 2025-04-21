package org.example.booking.controllers;

import org.example.booking.models.Homestay;
import org.example.booking.services.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/home")
public class Render {

    @Autowired
    private HomestayService homestayService;

    @GetMapping
    public String listHomestays(Model model) {
        List<Homestay> homestays = homestayService.findAll();
        model.addAttribute("homestays", homestays);
        return "home";
    }
    @GetMapping("/ManageHomestays")
    public String manageHomestays(Model model) {
        List<Homestay> homestays = homestayService.findAll();
        model.addAttribute("homestays", homestays);
        return "ManageHomestays"; // tên file .html bạn muốn render
    }
    @GetMapping("/homestay/{id}")
    public String showHomestayDetail(@PathVariable Long id, Model model) {
        Homestay homestay = homestayService.findById(id);
        model.addAttribute("homestay", homestay);
        return "homestay-detail";
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
}