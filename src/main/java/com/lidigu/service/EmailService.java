package com.lidigu.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    public void sendOTP(String email, String name, String otp) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("support@pixeldev.in", "JetShop");
        helper.setTo(email);
        helper.setSubject("Verify your email - JetShop");

        Resource resource = resourceLoader.getResource("classpath:templates/email_template.html");
        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        content = content.replace("{USERNAME}", name)
                         .replace("{OTP_CODE}", otp)
                         .replace("{YEAR}", String.valueOf(LocalDate.now().getYear()));

        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendOrderConfirmation(String email, String name, String orderId, String orderDetailsHtml) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("support@pixeldev.in", "JetShop");
        helper.setTo(email);
        helper.setSubject("Order Confirmed - JetShop");

        // Assuming a similar template or logic for order confirmation
        String content = "<h1>Hello " + name + ",</h1>" +
                         "<p>Your order <b>" + orderId + "</b> has been confirmed.</p>" +
                         orderDetailsHtml;

        helper.setText(content, true);
        mailSender.send(message);
    }
}
