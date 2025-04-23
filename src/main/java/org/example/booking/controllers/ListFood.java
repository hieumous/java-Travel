/*package org.example.booking.controllers;

import org.example.booking.models.ListFood;
import org.example.booking.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ListFood {

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/list-food")
    public String showManageFoodsPage(@RequestParam(value = "homestayId", required = false) Long homestayId, Model model) {
        List<org.example.booking.models.ListFood> foodList;
        if (homestayId != null) {
            foodList = foodRepository.findByHomestayId(homestayId);
        } else {
            foodList = foodRepository.findAll();
        }
        model.addAttribute("foodList", foodList);
        model.addAttribute("homestayId", homestayId);
        return "list-food";
    }
}

 */