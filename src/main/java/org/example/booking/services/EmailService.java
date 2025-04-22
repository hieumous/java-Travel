package org.example.booking.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
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

    public void sendEmailWithQR(String to, String subject, String body, String qrContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("nguyengiang2005e@gmail.com");

            // Tạo mã QR từ nội dung
            byte[] qrImage = generateQRCodeImage(qrContent, 200, 200);
            InputStreamSource imageSource = new ByteArrayResource(qrImage);

            // Nội dung email có nhúng ảnh QR
            String htmlBody = "<p>" + body + "</p><br><img src='cid:qrCodeImage'>";

            helper.setText(htmlBody, true); // gửi HTML
            helper.addInline("qrCodeImage", imageSource, "image/png");

            mailSender.send(message);
            System.out.println("Email đã gửi thành công đến " + to);
        } catch (MessagingException | IOException | WriterException e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }

    // Hàm tạo ảnh QR
    private byte[] generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}