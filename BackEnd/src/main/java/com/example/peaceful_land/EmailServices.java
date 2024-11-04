package com.example.peaceful_land;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendEmail (
            String to,
            String subject,
            String text,
            boolean isHtmlContent,
            String attachmentFilePath
    ){
        // Implement the logic to send an email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
            return true;
        } catch (MailException e) {
            System.out.println("Error sending email: " + e.getMessage());
            return false;
        }
    }

}
