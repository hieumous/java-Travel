package org.example.booking.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("nguyengiang2005e@gmail.com");
            helper.setText(body, true);
            mailSender.send(message);
            System.out.println("Email đã gửi thành công đến " + to);
        } catch (MessagingException e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }

    public void sendEmailWithQR(String to, String subject, String body, String qrContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("nguyengiang2005e@gmail.com");
            String htmlBody = "<p>" + body + "</p><br><img src='cid:qrCodeImage'>";
            helper.setText(htmlBody, true);
            byte[] qrImage = generateQRCodeImage(qrContent, 200, 200);
            InputStreamSource imageSource = new ByteArrayResource(qrImage);
            helper.addInline("qrCodeImage", imageSource, "image/png");
            mailSender.send(message);
            System.out.println("Email với QR đã gửi thành công đến " + to);
        } catch (MessagingException | IOException | WriterException e) {
            System.err.println("Gửi email với QR thất bại: " + e.getMessage());
        }
    }

    private byte[] generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}