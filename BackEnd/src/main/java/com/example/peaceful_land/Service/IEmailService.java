package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.EmailDetail;

import java.time.LocalDateTime;

// Chương trình Java minh họa việc tạo một Interface dịch vụ

public interface IEmailService {
    // Gửi email đơn giản
    void sendSimpleMail(EmailDetail details);

    // Gửi email đính kèm file
    String sendMailWithAttachment(EmailDetail details);

    // Gửi email với template xác nhận tài khoản
    void sendForgotPassVerifyEmail(String emailTo, String VerificationCode);

    // Gửi email với template xác nhận bài rao được duyệt đến người rao bài
    void sendPostApprovedEmailToOwner(String emailTo, Long postId, LocalDateTime createdAt);

    // Gửi email thông báo cho người quan tâm
    void sendPostApprovedEmailToWhoInterested(String emailTo, Long postId, LocalDateTime createdAt);

}
