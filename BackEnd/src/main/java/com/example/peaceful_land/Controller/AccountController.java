package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Repository.PaymentMethodRepository;
import com.example.peaceful_land.Service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/account")
public class AccountController {

    private final IAccountService accountService;
    private final AccountRepository accountRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @PostMapping("/info")
    public ResponseEntity<?> getAccountInfo(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.getAccountInfo(request.getUserId()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(accountService.changePassword(request));
    }

    @PostMapping("/purchase-role")
    public ResponseEntity<?> purchaseRole(@RequestBody PurchaseRoleRequest request) {
        return ResponseEntity.ok(accountService.purchaseRole(request));
    }

    @PostMapping(value = "/change-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@ModelAttribute ChangeAvatarRequest request) {
        return ResponseEntity.ok(accountService.changeAvatar(request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody AddPaymentMethodRequest request) {
        // TODO: Thực hiện chức năng thanh toán trước
        return ResponseEntity.ok(accountService.addPaymentMethod(request));
    }

    @PostMapping("/payment-methods")
    public ResponseEntity<?> getPaymentMethods(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.getPaymentMethod(request.getUserId()));
    }

    @DeleteMapping("/payment-method/delete")
    public ResponseEntity<?> deletePaymentMethod(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.deleteSoftPaymentMethod(request.getUserId(), request.getPaymentMethodId()));
    }

    // Dùng cho mục đích kiểm tra
    @GetMapping("/check-post-permission")
    public ResponseEntity<?> checkPostPermission(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.checkPostPermission(request.getUserId()));
    }

}
