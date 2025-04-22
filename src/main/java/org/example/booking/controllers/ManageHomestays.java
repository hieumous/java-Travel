package org.example.booking.controllers;

import org.example.booking.models.Homestay;
import org.example.booking.services.HomestayService;
import org.springframework.ui.Model;

import org.example.booking.models.Amenity;
import org.example.booking.services.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManageHomestays {

    @Autowired
    private HomestayService homestayService;

    @GetMapping("/ManageHomestays")
    public String showAllForAdmin(Model model) {
        List<Homestay> all = homestayService.findAll();
        model.addAttribute("homestays", all);
        return "ManageHomestays"; // ManageHomestays.html: dành cho admin duyệt
    }
}