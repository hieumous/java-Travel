package org.example.booking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManageHomestays {
    @GetMapping("/ManageHomestays")
    public String HomePage() {
        return "ManageHomestays";
    }
}