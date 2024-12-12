package com.example.peaceful_land.Service.repos;

import com.example.peaceful_land.DTO.EmailDetail;
import com.example.peaceful_land.Entity.PaymentMethod;
import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.RequestReport;

import java.time.LocalDateTime;

// Chương trình Java minh họa việc tạo một Interface dịch vụ

public interface IEmailService {
    // Gửi email đơn giản
    void sendSimpleMail(EmailDetail details);

    // Gửi email đính kèm file
    String sendMailWithAttachment(EmailDetail details);

    // Gửi email với template xác nhận tài khoản
    void sendForgotPassVerifyEmail(String emailTo, String VerificationCode);

    // Gửi email với template gửi bài rao
    void sendPostRequestConfirmation(Post post);

    // Gửi email với template xác nhận bài rao được duyệt đến người rao bài
    void sendPostApprovedEmailToOwner(String emailTo, Long postId, LocalDateTime createdAt);

    // Gửi email thông báo cho người quan tâm
    void sendPostApprovedEmailToWhoInterested(String emailTo, Long postId, LocalDateTime createdAt);

    // Gửi email với template xác nhận bài rao bị từ chối đến người rao bài
    void sendPostRejectedEmailToOwner(String emailTo, Long postId, LocalDateTime createdAt, String reason);

    // Gửi email thông báo cho người quan tâm răng bài rao được cập nhật
    void sendPostUpdatedEmailToWhoInterested(String emailTo, Long postId, LocalDateTime createdAt, String contentUpdate);

    // Gửi email biên lai rút tiền
    void sendWithdrawReceipt(String emailTo, Long id, LocalDateTime date, Long amount, PaymentMethod payment);

    // Gửi email phản hồi rút tiền
    void sendWithdrawResponse(String emailTo, Long id, LocalDateTime date, Long amount, PaymentMethod payment, boolean status, String denyMessageIfFalse);

    // Gửi email thông báo yêu cầu đã được xử lý
    void sendRequestHandledEmail(RequestReport request, String replyMessage);

    // Gửi email thông báo khóa tài khoản
    void sendAccountLockedEmail(String emailTo, String reason);

    // Gửi email xóa bài rao
    void sendPostDeletedEmail(String emailTo, Long postId, LocalDateTime createdAt, String reason);

}
