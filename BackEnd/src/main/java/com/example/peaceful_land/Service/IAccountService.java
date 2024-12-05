package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PurchaseRoleRequest;
import com.example.peaceful_land.DTO.RegisterRequest;
import com.example.peaceful_land.Entity.Account;

public interface IAccountService {
    String tryLogin(String userId, String password);
    Account register(RegisterRequest userInfo);
    void forgotPassword(String email);
    void verifyOtp(String email, String otp);
    void resetPassword(String email, String newPassword);
    boolean resetRoleIfExpired(Long id);
    Account purchaseRole(PurchaseRoleRequest id);
}