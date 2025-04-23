package org.example.booking.controllers;

import org.example.booking.models.User;
import org.example.booking.models.Booking;
import org.example.booking.models.Homestay;
import org.example.booking.services.UserService;
import org.example.booking.services.BookingService;
import org.example.booking.services.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HomestayService homestayService;

    @GetMapping
    public String getProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        model.addAttribute("user", user);

        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"))) {
            List<Booking> bookings = bookingService.getBookingsByUserId(user.getId());
            model.addAttribute("bookings", bookings);
        } else if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_OWNER"))) {
            List<Homestay> homestays = homestayService.getHomestaysByOwnerId(user.getId());
            model.addAttribute("homestays", homestays);
        }
        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user, @RequestParam(required = false) String password) {
        userService.updateUserProfile(user, password);
        return "redirect:/profile";
    }
}