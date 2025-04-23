package org.example.booking.controllers;
import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.services.BookingService;
import org.example.booking.services.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller

public class ListRoom {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/list-room")
    public String showListRoom(Model model) {
        List<Booking> bookings = bookingService.findAll(); // hoặc bookingRepository.findAll()
        model.addAttribute("bookings", bookings);
        return "list-room"; // list-room.html nằm trong src/main/resources/templates/
    }
    @PostMapping("/delete/{id}")
    public String deleteHomestay(@PathVariable Long id) {
        bookingService.deleteById(id);
        return "redirect:/list-room";
    }
}
