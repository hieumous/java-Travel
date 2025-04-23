package org.example.booking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class about {
    @GetMapping("/about")
    public String aboutPage() {
        return "about"; // sẽ trả về file about.html trong thư mục templates
    }
}
