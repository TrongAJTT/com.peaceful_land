package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.EmailDetails;

// Chương trình Java minh họa việc tạo một Interface dịch vụ

public interface IEmailService {
    // Gửi email đơn giản
    String sendSimpleMail(EmailDetails details);

    // Gửi email đính kèm file
    String sendMailWithAttachment(EmailDetails details);

    String sendEmailConfirmation(String emailTo, String VerificationCode);
}
