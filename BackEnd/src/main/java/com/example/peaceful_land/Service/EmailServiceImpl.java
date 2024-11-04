package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value ("${spring.mail.username}") private String sender;

    public static final String SUCCESS = "Mail Sent Successfully!";

    @Override
    public String sendSimpleMail(EmailDetails details) {
        // Try block to check for exceptions
        try {
            System.out.println("Sending Email...");
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getToEmail());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return SUCCESS;
        }

        // Catch block to handle the exceptions
        catch (MailException e) {
            return e.getMessage();
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            // Setting multipart as true for attachments to be sent
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getToEmail());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(
                    new File(details.getAttachment())
            );

            mimeMessageHelper.addAttachment(
                    Objects.requireNonNull(file.getFilename()), file
            );

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return SUCCESS;
        }

        // Catch block to handle MessagingException
        catch (MessagingException | MailException e) {
            return e.getMessage();
        }
    }

    @Override
    public String sendEmailConfirmation(String emailTo, String VerificationCode) {
        try {
            System.out.println("Sending Email...");
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailTo);
            mailMessage.setText("Your verification code is: " + VerificationCode);
            mailMessage.setSubject("Verification Code");

            // Sending the mail
            javaMailSender.send(mailMessage);
            return SUCCESS;
        }
        catch (MailException e) {
            return e.getMessage();
        }
    }
}
