package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Account;

public interface IAccountService {

    // Lấy thông tin tài khoản
    AccountInfoResponse getAccountInfo(Long userId);

    // Đăng nhập
    String tryLogin(String userId, String password);

    // Đăng ký tài khoản mới
    Account register(RegisterRequest userInfo);

    // Đổi mật khẩu tài khoản
    String changePassword(ChangePasswordRequest request);

    // Quên mật khẩu giai đoạn 1: Gửi mã OTP
    void forgotPassword(String email);

    // Quên mật khẩu giai đoạn 2: Xác thực mã OTP
    void verifyOtp(String email, String otp);

    // Quên mật khẩu giai đoạn 3: Đặt lại mật khẩu
    void resetPassword(String email, String newPassword);

    // Đặt lại vai trò nếu đã hết hạn
    boolean resetRoleIfExpired(Long id);

    // Mua vai trò
    Account purchaseRole(PurchaseRoleRequest id);

    // Lấy thời gian rao bài tối đa (theo ngày)
    int getExpirationRange(String userId);

    // Lấy thời gian duyệt bài tối đa (theo ngày)
    int getApprovalRange(Byte role);

    // Thay đổi avatar
    String changeAvatar(ChangeAvatarRequest request);
}