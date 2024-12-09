package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.*;
import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.PaymentMethod;
import com.example.peaceful_land.Entity.Purchase;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Repository.PaymentMethodRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import com.example.peaceful_land.Repository.PurchaseRepository;
import com.example.peaceful_land.Utils.ImageUtils;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.example.peaceful_land.Utils.VariableUtils.TYPE_UPLOAD_AVATAR;

@Service @RequiredArgsConstructor
public class AccountService implements IAccountService{

    private final AccountRepository accountRepository;
    private final PurchaseRepository purchaseRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final EmailService emailService;
    private final RedisService redisService;
    private final PropertyRepository propertyRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public AccountInfoResponse getAccountInfo(Long userId) {
        return accountRepository.findById(userId)
                .map(AccountInfoResponse::from)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
    }

    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public Account tryLogin(String userId, String password) {
        // userId có thể là email hoặc số điện thoại
        for(Account account:getAccounts()){
            if((account.getEmail().equals(userId) || account.getPhone().equals(userId)) &&
                    passwordEncoder.matches(password,account.getPassword())){
                return account;
            }
        }
        throw new RuntimeException("Thông tin đăng nhập không chính xác");
    }

    @Override
    public Account register(AccountPrimaryInfo userInfo) {
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
                        .password(passwordEncoder.encode(userInfo.getPassword()))
                        .accountBalance(0)
                        .name(userInfo.getName())
                        .birthDate(userInfo.getBirthDate())
                        .phone(userInfo.getPhone())
                        .status(true)
                        .avatarUrl(VariableUtils.DEFAULT_AVATAR)
                        .roleExpiration(LocalDate.of(9999, 12, 31))
                        .build()
        );
    }

    public void encodePassword(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }

    public void encodeOldPassword(){
        List<Account> accountList = getAccounts();
        for(Account account:accountList){
            if (!account.getPassword().startsWith("$2a$") &&
                    !account.getPassword().startsWith("$2b$") &&
                    !account.getPassword().startsWith("$2y$")){
                encodePassword(account);
            }
        }
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        return accountRepository.findById(request.getUserId())
                .map(account -> {
                    if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
                        throw new RuntimeException("Mật khẩu cũ không chính xác");
                    }
                    account.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    accountRepository.save(account);
                    return "Đổi mật khẩu thành công";
                })
                .orElseThrow(() -> new IllegalStateException("Tài khoản không tồn tại"));
    }

    @Override
    public String changeAccountInfo(AccountPrimaryInfo request) {
        return "";
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
            account.setPassword(passwordEncoder.encode(newPassword));
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
        Long requiredMoney = VariableUtils.getRolePriceFromDayRange(request.getRole(), request.getDay());
        if (account.getAccountBalance() < requiredMoney) {
            throw new RuntimeException("Số dư không đủ. Yêu cầu tối thiểu " + requiredMoney);
        }
        // Nếu đã có role thì cộng thêm thời gian
        if (account.getRole() == request.getRole()){
            account.setRoleExpiration(account.getRoleExpiration().plusDays(request.getDay()));
            // Lưu thông tin giao dịch
            purchaseRepository.save(Purchase.builder()
                    .user(account)
                    .action(VariableUtils.PURCHASE_ACTION_EXTEND_ROLE)
                    .amount(requiredMoney)
                    .build()
            );
        }
        // Nếu role mới cao hơn role cũ thì cập nhật role mới và thời gian
        else if (account.getRole() < request.getRole()){
            account.setRole(request.getRole());
            account.setRoleExpiration(LocalDate.now().plusDays(request.getDay()));
            // Lưu thông tin giao dịch
            purchaseRepository.save(Purchase.builder()
                    .user(account)
                    .action(Objects.equals(request.getRole(), VariableUtils.ROLE_BROKER) ?
                            VariableUtils.PURCHASE_ACTION_BROKER :
                            VariableUtils.PURCHASE_ACTION_BROKER_VIP)
                    .amount(requiredMoney)
                    .build()
            );
        }
        else {
            throw new RuntimeException("Không thể mua role thấp hơn role hiện tại");
        }
        account.setAccountBalance(account.getAccountBalance() - requiredMoney);
        return accountRepository.save(account);
    }

    @Override
    public String changeAvatar(ChangeAvatarRequest request) {
        // Kiểm tra tài khoản tồn tại
        Account account = accountRepository.findById(request.getUserId()).orElse(null);
        if (account == null) throw new RuntimeException("Tài khoản không tồn tại");
        // Kiểm tra file hợp lệ
        MultipartFile file = request.getImage();
        ImageUtils.checkImageFile(file);
        // Tạo thư mục upload nếu chưa tồn tại
        try {
            ImageUtils.createUploadDirIfNotExists(TYPE_UPLOAD_AVATAR);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo thư mục upload: " + e.getMessage());
        }
        // Thực hiện thay đổi avatar
        try {
            // Lưu file vào server
            String fileName = ImageUtils.saveFileServer(file, TYPE_UPLOAD_AVATAR);
            // Xóa file cũ
            if (!account.getAvatarUrl().equals(VariableUtils.DEFAULT_AVATAR)) {
                ImageUtils.deleteFileServer(account.getAvatarUrl());
            }
            // Cập nhật đường dẫn file mới vào database
            account.setAvatarUrl(fileName);
            accountRepository.save(account);
            // Trả về thông báo thành công
            return "Đổi avatar thành công. Đường dẫn mới: " + account.getAvatarUrl();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu tập tin: " + e.getMessage());
        }
    }

    @Override
    public String addPaymentMethod(AddPaymentMethodRequest request) {
        // TODO: Thực hiện chức năng thanh toán trước sau đó mới cấp quyền cho cái này
        // Kiểm tra tài khoản có tồn tại không
        Account account = accountRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        // Kiểm tra phương thức thanh toán đã tồn tại chưa
        if (paymentMethodRepository.existsByAccountEqualsAndNameEqualsAndAccountNumberEqualsAndHideEquals(account, request.getName(), request.getAccountNumber(), false)) {
            throw new RuntimeException("Phương thức thanh toán đã tồn tại.");
        }
        // Lưu phương thức thanh toán
        paymentMethodRepository.save(
                PaymentMethod.builder()
                        .account(account)
                        .isWallet(request.getIsWallet())
                        .name(request.getName())
                        .accountNumber(request.getAccountNumber())
                        .build()
        );
        return "Thêm phương thức thanh toán thành công";
    }

    @Override
    public List<PaymentMethodResponse> getPaymentMethod(Long userId) {
        return paymentMethodRepository.findAllByAccountEqualsAndHideEquals(
                        accountRepository
                                .findById(userId)
                                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại")),
                        false
                )
                .stream()
                .map(PaymentMethodResponse::from)
                .toList();
    }

    @Override
    public String deleteSoftPaymentMethod(Long userId, Long paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .map(paymentMethod -> {
                    if (paymentMethod.getAccount().getId() != userId) {
                        throw new RuntimeException("Tài khoản không tồn tại hoặc không có quyền xóa phương thức thanh toán này");
                    }
                    if (paymentMethod.getHide()) {
                        throw new RuntimeException("Phương thức thanh toán đã bị xóa");
                    }
                    paymentMethod.setHide(true);
                    paymentMethodRepository.save(paymentMethod);
                    return "Xóa phương thức thanh toán thành công";
                })
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không tồn tại"));
    }

    @Override
    public PostPermissionResponse checkPostPermission(Long userId) {
        // Kiểm tra tài khoản có tồn tại không
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại"));
        // Nếu là người dùng thông thường thì kiểm tra có vượt quá lượng bài đăng tối đa không
        if (account.getRole() == VariableUtils.ROLE_NORMAL) {
            long count = propertyRepository.countByUserEquals(account);
            if (count >= VariableUtils.MAX_POST_NORMAL_TOTAL) {
                throw new IllegalStateException("Vượt quá số lượng bài đăng tối đa");
            }
        }
        // Kiểm tra xem lượng bài đăng ngày hôm nay đã vượt quá giới hạn chưa?
        int countToday = (int) propertyRepository.countByDateBeginBetweenAndUserEquals(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(23, 59, 59),
                account
        );
        int maxPostPerDay = VariableUtils.getPostLimitPerDay(account.getRole());
        if (countToday >= maxPostPerDay) {
            throw new IllegalStateException("Đã đạt tối đa giới hạn bài đăng trong ngày: " + maxPostPerDay);
        }
        // Kiểm tra quyền chọn danh mục bất động sản
        boolean fullCategory = account.getRole() != VariableUtils.ROLE_NORMAL;
        // Lấy thời gian sống tối đa của bài rao
        int maxLiveTime = VariableUtils.getPostLiveTimeDay(account.getRole());
        // Trả về thông tin quyền đăng bài
        return PostPermissionResponse.builder()
                .maxLiveTime(maxLiveTime)
                .fullCategory(fullCategory)
                .build();
    }
}