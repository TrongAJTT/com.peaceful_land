package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.EmailDetail;
import com.example.peaceful_land.Entity.PaymentMethod;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

@Service @RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;

    @Value ("${spring.mail.username}")
    private String sender;

    public static final String SUCCESS = "Mail Sent Successfully!";

    @Override
    public void sendSimpleMail(EmailDetail details) {
        // Try block to check for exceptions
        try {
            System.out.println("[Mail proxy] Sending Simple Email");
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getToEmail());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
        }

        // Catch block to handle the exceptions
        catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetail details) {
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
    public void sendForgotPassVerifyEmail(String emailTo, String VerificationCode) {
        try {
            System.out.println("[Mail proxy] Sending Forgot Pass Verify Email to email: " + emailTo);
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailTo);
            mailMessage.setText("Your verification code is: " + VerificationCode);
            mailMessage.setSubject("Verification Code");

            // Sending the mail
            javaMailSender.send(mailMessage);
        }
        catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPostApprovedEmailToOwner(String emailTo, Long postId, LocalDateTime createdAt) {
        try {
            System.out.println("[Mail proxy] Sending Post Approved Email to owner: " + emailTo);
            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = String.format(
                    """
                    <p>Chúng tôi vui lòng thông báo với bạn rằng bài rao của bạn đã được duyệt.</p>
                    <p><b>Mã bài rao:</b> %s.</p>
                    <p><b>Ngày tạo:</b> %s.</p>
                    """, postId, VariableUtils.convertToVnTimeZoneString(createdAt));

            helper.setTo(emailTo);
            helper.setSubject("Bài rao của bạn đã được duyệt");
            helper.setText(htmlContent, true); // `true` để bật chế độ HTML

            // Sending the mail
            javaMailSender.send(mimeMessage);
        }
        catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPostApprovedEmailToWhoInterested(String emailTo, Long postId, LocalDateTime createdAt) {
        try {
            System.out.println("[Mail proxy] Sending Post Approved Email to interested user: " + emailTo);
            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = String.format(
                    """
                    <p>Chúng tôi vui lòng thông báo với bạn rằng một bài rao mà bạn quan tâm đã được duyệt.</p>
                    <p><b>Mã bài rao:</b> %s.</p>
                    <p><b>Ngày bắt đầu quan tâm:</b> %s.</p>
                    <p>Hãy kiểm tra ngay để biết thêm thông tin chi tiết về bất động sản bạn nhé!.</p>
                    """, postId, VariableUtils.convertToVnTimeZoneString(createdAt));

            helper.setTo(emailTo);
            helper.setSubject("Bài rao mà bạn quan tâm đã được duyệt");
            helper.setText(htmlContent, true); // `true` để bật chế độ HTML

            // Sending the mail
            javaMailSender.send(mimeMessage);
        }
        catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPostRejectedEmailToOwner(String emailTo, Long postId, LocalDateTime createdAt, String reason) {
        try {
            System.out.println("[Mail proxy] Sending Post Rejected Email to interested user: " + emailTo);
            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = String.format(
                    """
                    <p>Chúng tôi xin chia buồn thông báo với bạn rằng một bài rao của bạn đã bị từ chối duyệt.</p>
                    <p><b>Mã bài rao:</b> %s.</p>
                    <p><b>Ngày tạo:</b> %s.</p>
                    <p><b>Lý do:</b> %s.</p>
                    <p>Hãy chú ý hơn trong bài rao sau bạn nhé!.</p>
                    """, postId, VariableUtils.convertToVnTimeZoneString(createdAt), reason);

            helper.setTo(emailTo);
            helper.setSubject("Bài rao của bạn đã bị từ chối");
            helper.setText(htmlContent, true); // `true` để bật chế độ HTML

            // Sending the mail
            javaMailSender.send(mimeMessage);
        }
        catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPostUpdatedEmailToWhoInterested(String emailTo, Long postId, LocalDateTime createdAt, String contentUpdate) {
        try {
            System.out.println("[Mail proxy] Sending Notify Update Email to interested user: " + emailTo);
            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = String.format(
                    """
                    <p>Chúng tôi vui lòng thông báo với bạn rằng một bài rao mà bạn quan tâm đã được cập nhật.</p>
                    <p><b>Mã bài rao:</b> %s.</p>
                    <p><b>Thời gian bắt đầu quan tâm:</b> %s.</p>
                    <p><b>Nội dung cập nhật:</b> %s.</p>
                    """, postId, VariableUtils.convertToVnTimeZoneString(createdAt), contentUpdate);

            helper.setTo(emailTo);
            helper.setSubject("Bài rao mà bạn theo dõi đã được cập nhật");
            helper.setText(htmlContent, true);

            // Sending the mail
            javaMailSender.send(mimeMessage);
        }
        catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateWithdrawReceipt(Long id, LocalDateTime date, Long amount, String paymentInfo, String accountNumber) {
        return String.format(
                """
                <table style="border-collapse: collapse; margin: 25px 0; font-size: 0.9em; font-family: sans-serif; min-width: 400px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);">
                    <thead>
                        <tr style="background-color: #009879; color: #ffffff;">
                            <th style="padding: 12px 15px;">Mã yêu cầu</th>
                            <td style="padding: 12px 15px;">%s</td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr style="border-bottom: 1px solid #dddddd;">
                            <th style="padding: 12px 15px;">Ngày tạo</th>
                            <td style="padding: 12px 15px;">%s</td>
                        </tr>
                        <tr style="border-bottom: 1px solid #dddddd;">
                            <th style="padding: 12px 15px;">Số tiền rút</th>
                            <td style="padding: 12px 15px;">%s</td>
                        </tr>
                        <tr style = "border-bottom: 2px solid #dddddd;">
                            <th style="padding: 12px 15px;">Phương thức rút</th>
                            <td style="padding: 12px 15px;">%s - %s</td>
                        </tr>
                        <tr style="border-bottom: 1px solid #009879;">
                            <th style="padding: 12px 15px;">Trạng thái</th>
                            <td style="padding: 12px 15px;">to_be_replace</td>
                        </tr>
                        to_be_replace
                    </tbody>
                </table>
                """,
                id, VariableUtils.convertToVnTimeZoneString(date), amount, paymentInfo, accountNumber
        );
    }

    @Override
    public void sendWithdrawReceipt(String emailTo, Long id, LocalDateTime date, Long amount, PaymentMethod payment) {
        try {
            System.out.println("[Mail proxy] Sending Notify Update Email to interested user: " + emailTo);
            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent =
                    ("<p>Chúng tôi vui lòng thông báo với bạn rằng yêu cầu rút tiền của bạn đang được xử lý.</p>" +
                    generateWithdrawReceipt(id, date, amount, payment.getMethodAndNameString(), payment.getAccountNumber()) +
                    "<p>Kết quả xử lý sẽ có trong vòng tối đa 48 giờ kế tiếp. Hãy chú ý theo dõi email của bạn</p>")
                            .replaceFirst("to_be_replace", "Đang xử lý")
                            .replaceFirst("to_be_replace", "");

            helper.setTo(emailTo);
            helper.setSubject("Biên lai cho yêu cầu rút tiền của bạn");
            helper.setText(htmlContent, true);

            // Sending the mail
            javaMailSender.send(mimeMessage);
        }
        catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendWithdrawResponse(String emailTo, Long id, LocalDateTime date, Long amount, PaymentMethod payment,
                                     boolean status, String denyMessage) {
        try {
            System.out.println("[Mail proxy] Sending Notify Update Email to interested user: " + emailTo);
            // Creating a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent =
                    ("<p>Chúng tôi vui lòng thông báo với bạn rằng yêu cầu rút tiền của bạn đã được xử lý.</p>" +
                            generateWithdrawReceipt(id, date, amount, payment.getMethodAndNameString(), payment.getAccountNumber()))
                            .replaceFirst("to_be_replace", status ? "Đã duyệt" : "Đã từ chối")
                            .replace("009879;\"", "dddddd;\"")
                            .replaceFirst("to_be_replace", status ? "" :
                                    "<tr style=\"border-bottom: 1px solid #009879;\">" +
                                    "<th style=\"padding: 12px 15px;\">Lý do từ chối</th>" +
                                    "<td style=\"padding: 12px 15px;\">"+denyMessage+"</td></tr>");

            helper.setTo(emailTo);
            helper.setSubject("Thông báo kết quả cho yêu cầu rút tiền của bạn");
            helper.setText(htmlContent, true);

            // Sending the mail
            javaMailSender.send(mimeMessage);
        }
        catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
