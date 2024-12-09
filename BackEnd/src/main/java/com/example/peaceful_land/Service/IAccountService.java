package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Account;

import java.util.List;

public interface IAccountService {

    // Lấy thông tin tài khoản
    AccountInfoResponse getAccountInfo(Long userId);

    // Đăng nhập
    Account tryLogin(String userId, String password);

    // Đăng ký tài khoản mới
    Account register(AccountPrimaryInfo userInfo);

    // Đổi mật khẩu tài khoản
    String changePassword(ChangePasswordRequest request);

    // Cập nhật thông tin tài khoản
    String updateAccountInfo(UpdateAccountInfoRequest request);

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

    // Thay đổi avatar
    String changeAvatar(ChangeAvatarRequest request);

    // Thêm phương thức thanh toán
    String addPaymentMethod(AddPaymentMethodRequest request);

    // Xem danh sách phương thức thanh toán
    List<PaymentMethodResponse> getPaymentMethod(Long userId);

    // Xóa mềm phương thức thanh toán
    String deleteSoftPaymentMethod(Long userId, Long paymentMethodId);

    // Kiểm tra khả năng đăng bài rao
    PostPermissionResponse checkPostPermission(Long userId);

}