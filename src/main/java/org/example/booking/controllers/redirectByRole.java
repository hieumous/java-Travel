package org.example.booking.controllers;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class redirectByRole {
    @GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"))) {
            return "redirect:/ManageHomestays";
        }
        return "redirect:/";
    }
}