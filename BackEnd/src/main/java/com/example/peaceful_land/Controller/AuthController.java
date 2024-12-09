package com.example.peaceful_land.Controller;


import com.example.peaceful_land.DTO.LoginRequest;
import com.example.peaceful_land.DTO.ResetPasswordRequest;
import com.example.peaceful_land.DTO.AccountPrimaryInfo;
import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Security.JwtResponse;
import com.example.peaceful_land.Security.JwtTokenProvider;
import com.example.peaceful_land.Service.IAccountService;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final IAccountService accountService;
    private final Gson gson;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Trả về lỗi nếu có lỗi xác thực
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
            // Generate JWT token
        Account account = accountService.tryLogin(loginRequest.getUserId(),loginRequest.getPassword());
        String jwtToken = jwtTokenProvider.generateToken(account.getEmail());

        // Return the JWT token along with user details (or just the token if preferred)
        return ResponseEntity.ok(new JwtResponse(jwtToken,account));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountPrimaryInfo userInfo) {
        return ResponseEntity.ok(accountService.register(userInfo));
    }

    // Quên mật khẩu: Gửi mã xác thực về email
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ResetPasswordRequest request) {
        accountService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(gson.toJson("Đã gửi mã xác thực về email"));
    }

    // Quên mật khẩu: Xác thực mã OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody ResetPasswordRequest request) {
        accountService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(gson.toJson("Xác thực thành công"));
    }

    // Quên mật khẩu: Đặt lại mật khẩu mới
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        accountService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(gson.toJson("Đặt lại mật khẩu thành công"));
    }

    // Đăng xuất
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(gson.toJson("Đăng xuất thành công"));
    }


}
