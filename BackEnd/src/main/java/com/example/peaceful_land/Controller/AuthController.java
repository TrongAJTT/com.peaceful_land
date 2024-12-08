package com.example.peaceful_land.Controller;


import com.example.peaceful_land.DTO.LoginRequest;
import com.example.peaceful_land.DTO.ResetPasswordRequest;
import com.example.peaceful_land.DTO.RegisterRequest;
import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Security.JwtResponse;
import com.example.peaceful_land.Security.JwtTokenProvider;
import com.example.peaceful_land.Service.IAccountService;
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

//    @PostMapping("/login")
//    public ResponseEntity<?> login(String userId, String password) {
//        // accountService.tryLogin(userId, password);
//        return ResponseEntity.ok("Đăng nhập thành công");
//    }

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
    public ResponseEntity<?> register(@RequestBody RegisterRequest userInfo) {
        try {
            return ResponseEntity.ok(accountService.register(userInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Quên mật khẩu: Gửi mã xác thực về email TODO: Thay thế email thành JWT Token
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ResetPasswordRequest request) {
        try {
            accountService.forgotPassword(request.getEmail());
            return ResponseEntity.ok("Đã gửi mã xác thực về email");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Quên mật khẩu: Xác thực mã OTP TODO: Thay thế email thành JWT Token
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody ResetPasswordRequest request) {
        try {
            accountService.verifyOtp(request.getEmail(), request.getOtp());
            return ResponseEntity.ok("Xác thực thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Quên mật khẩu: Đặt lại mật khẩu mới TODO: Thêm kiểm tra Token đã hết hạn trước khi cho phép đổi mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            accountService.resetPassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok("Đặt lại mật khẩu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
