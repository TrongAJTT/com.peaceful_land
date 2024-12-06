package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Repository.PaymentMethodRepository;
import com.example.peaceful_land.Service.IAccountService;
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
        try {
            return ResponseEntity.ok(accountService.getAccountInfo(request.getUserId()));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            return ResponseEntity.ok(accountService.changePassword(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/purchase-role")
    public ResponseEntity<?> purchaseRole(@RequestBody PurchaseRoleRequest request) {
        try {
            return ResponseEntity.ok(accountService.purchaseRole(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/change-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@ModelAttribute ChangeAvatarRequest request) {
        try {
            return ResponseEntity.ok(accountService.changeAvatar(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody AddPaymentMethodRequest request) {
        try {
            // TODO: Thực hiện chức năng thanh toán trước
            return ResponseEntity.ok(accountService.addPaymentMethod(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/payment-methods")
    public ResponseEntity<?> getPaymentMethods(@RequestBody IdRequest request) {
        try {
            return ResponseEntity.ok(accountService.getPaymentMethod(request.getUserId()));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/payment-method/delete")
    public ResponseEntity<?> deletePaymentMethod(@RequestBody IdRequest request) {
        try {
            return ResponseEntity.ok(accountService.deleteSoftPaymentMethod(request.getUserId(), request.getPaymentMethodId()));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
