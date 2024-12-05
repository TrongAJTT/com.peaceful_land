package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PurchaseRoleRequest;
import com.example.peaceful_land.DTO.RegisterRequest;
import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Utils.PriceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service @RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final RedisService redisService;

    @Override
    public String tryLogin(String userId, String password) {
        // userId có thể là email hoặc số điện thoại
        // Xử lý ngoại lệ

        // Xử lý và trả về Token nếu hợp lệ
        return "token";
    }

    @Override
    public Account register(RegisterRequest userInfo) {
        if (accountRepository.existsByEmail(userInfo.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        else if (accountRepository.existsByPhone(userInfo.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        // TODO: Mã hóa mật khẩu
        return accountRepository.save(
                Account.builder()
                        .role((byte) 0)
                        .email(userInfo.getEmail())
                        .password(userInfo.getPassword())   // TODO: Mã hóa mật khẩu
                        .accountBalance(0)
                        .name(userInfo.getName())
                        .birthDate(userInfo.getBirthDate())
                        .phone(userInfo.getPhone())
                        .status(true)
                        .avatar(1L)
                        .roleExpiration(LocalDate.of(9999, 12, 31))
                        .build()
        );
    }

    @Override
    public void forgotPassword(String email) {
        if (!accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email không tồn tại");
        }
        // Tạo mã OTP có 6 ksy tự
        String otp = String.valueOf((int) (Math.random() * 900000 + 100000));
        // Lưu mã OTP vào Redis với email là key, thời gian sống là 5 phút
        redisService.storeOtp(email, otp, 300);
        // Gửi email chứa mã OTP
        emailService.sendForgotPassVerifyEmail(email, otp);
    }

    @Override
    public void verifyOtp(String email, String otp) {
        if (!accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email không hợp lệ");
        }
        if (!redisService.verifyOtp(email, otp)) {
            throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn");
        }
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        if (!accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email không hợp lệ");
        }
        accountRepository.findByEmail(email).ifPresent(account -> {
            account.setPassword(newPassword); // TODO: Mã hóa mật khẩu
            accountRepository.save(account);
        });
    }

    @Override
    public boolean resetRoleIfExpired(Long id) {
        return accountRepository.findById(id).map(account -> {
            if (account.getRoleExpiration().isBefore(LocalDate.now())) {
                account.setRole((byte) 0);
                account.setRoleExpiration(LocalDate.of(9999, 12, 31));
                accountRepository.save(account);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    public Account purchaseRole(PurchaseRoleRequest request) {
        Account account = accountRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        Long requiredMoney = PriceUtils.getRolePriceFromDayRange(request.getRole(), request.getDay());
        if (account.getAccountBalance() < requiredMoney) {
            throw new RuntimeException("Số dư không đủ. Yêu cầu tối thiểu " + requiredMoney);
        }
        // Nếu đã có role thì cộng thêm thời gian
        if (account.getRole() == request.getRole()){
            account.setRoleExpiration(account.getRoleExpiration().plusDays(request.getDay()));
        }
        // Nếu role mới cao hơn role cũ thì cập nhật role mới và thời gian
        else if (account.getRole() < request.getRole()){
            account.setRole(request.getRole());
            account.setRoleExpiration(LocalDate.now().plusDays(request.getDay()));
        }
        else {
            throw new RuntimeException("Không thể mua role thấp hơn role hiện tại");
        }
        account.setAccountBalance(account.getAccountBalance() - requiredMoney);
        return accountRepository.save(account);
    }
}