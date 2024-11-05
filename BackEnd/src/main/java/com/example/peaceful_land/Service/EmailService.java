package com.example.peaceful_land.Service;

// Chương trình Java minh họa việc tạo một Interface dịch vụ

import com.example.peaceful_land.Model.EmailDetails;

public interface EmailService {
    // Gửi email đơn giản
    String sendSimpleMail(EmailDetails details);

    // Gửi email đính kèm file
    String sendMailWithAttachment(EmailDetails details);

    String sendEmailConfirmation(String emailTo, String VerificationCode);
}
