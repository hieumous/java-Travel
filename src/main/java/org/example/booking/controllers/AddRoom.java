package org.example.booking.controllers;
import org.example.booking.models.Homestay;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
@Controller
@RequestMapping("/homestay")
public class AddRoom {

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("homestay", new Homestay());
        return "add-homestay";
    }
}
