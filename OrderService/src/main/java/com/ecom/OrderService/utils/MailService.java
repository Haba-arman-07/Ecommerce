package com.ecom.OrderService.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setFrom("habaarman516@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, isHtml); // **true = HTML, false = plain text**

            mailSender.send(message);
            System.out.println("Mail sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
