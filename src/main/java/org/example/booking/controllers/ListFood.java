package org.example.booking.controllers;

import org.example.booking.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ListFood {
    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/list-food") // hoặc /manage-foods nếu bạn đổi tên
    public String showManageFoodsPage(Model model) {
        List<org.example.booking.models.ListFood> foodList = foodRepository.findAll();
        model.addAttribute("foodList", foodList);
        return "list-food"; // tên file thymeleaf
    }

}



