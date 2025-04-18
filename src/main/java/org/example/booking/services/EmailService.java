package org.example.booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("nguyengiang2005e@gmail.com"); // hoặc lấy từ cấu hình

            mailSender.send(message);
            System.out.println("Email đã gửi thành công đến " + to);
        } catch (Exception e) {
            System.err.println(" Gửi email thất bại: " + e.getMessage());
        }
    }
}
