package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Service.repos.IAccountService;
import com.google.gson.Gson;
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
    private final Gson gson;

    @PostMapping("/info")
    public ResponseEntity<?> getAccountInfo(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.getAccountInfo(request.getUserId()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(gson.toJson(accountService.changePassword(request)));
    }

    @PostMapping("/purchase-role")
    public ResponseEntity<?> purchaseRole(@RequestBody PurchaseRoleRequest request) {
        return ResponseEntity.ok(accountService.purchaseRole(request));
    }

    @PostMapping(value = "/change-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@ModelAttribute ChangeAvatarRequest request) {
        return ResponseEntity.ok(gson.toJson(accountService.changeAvatar(request)));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody AddPaymentMethodRequest request) {
        // TODO: Thực hiện chức năng thanh toán trước
        return ResponseEntity.ok(gson.toJson(accountService.addPaymentMethod(request)));
    }

    @PostMapping("/payment-methods")
    public ResponseEntity<?> getPaymentMethods(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.getPaymentMethod(request.getUserId()));
    }

    @DeleteMapping("/payment-method/delete")
    public ResponseEntity<?> deletePaymentMethod(@RequestBody IdRequest request) {
        return ResponseEntity.ok(gson.toJson(
                accountService.deleteSoftPaymentMethod(request.getUserId(), request.getPaymentMethodId())
        ));
    }

    @PostMapping("/check-post-permission")
    public ResponseEntity<?> checkPostPermission(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.checkPostPermission(request.getUserId()));
    }

    @PostMapping("/update-info")
    public ResponseEntity<?> updateInfo(@RequestBody UpdateAccountInfoRequest request) {
        return ResponseEntity.ok(gson.toJson(accountService.updateAccountInfo(request)));
    }

    @PostMapping("/request-withdraw")
    public ResponseEntity<?> requestWithdraw(@RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(gson.toJson(accountService.createWithdrawRequest(request)));
    }

    @PostMapping("/history-purchases")
    public ResponseEntity<?> getHistoryPurchases(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.viewPurchasesHistory(request.getUserId()));
    }

    @PostMapping("/statistics")
    public ResponseEntity<?> getStatistics(@RequestBody IdRequest request) {
        return ResponseEntity.ok(accountService.getStatistics(request.getUserId()));
    }

    @PostMapping("/admin/ban")
    public ResponseEntity<?> banAccount(@RequestBody AdminBanRemoveRequest request) {
        return ResponseEntity.ok(gson.toJson(accountService.banAccount(request)));
    }

}
