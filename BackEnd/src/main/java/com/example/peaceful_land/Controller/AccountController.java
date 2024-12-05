package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.ChangeAvatarRequest;
import com.example.peaceful_land.DTO.PurchaseRoleRequest;
import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Service.IAccountService;
import com.example.peaceful_land.Utils.ImageUtils;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.peaceful_land.Utils.VariableUtils.TYPE_UPLOAD_AVATAR;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/account")
public class AccountController {

    private final IAccountService accountService;
    private final AccountRepository accountRepository;

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
        Account account = accountRepository.findById(request.getUserId())
                .orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
        MultipartFile file = request.getImage();

        // Kiểm tra file rỗng
        if (file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is required");
        }
        // Kiểm tra kích thước file (nax 5MB)
        if(file.getSize() > 5*1024*1024){
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large!! Size must be less than 5MB");
        }
        // Kiểm tra content type
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")){
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Unsupported file type. Only support image file");
        }
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
            return ResponseEntity.ok("File uploaded successfully with new path: " + account.getAvatarUrl());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while saving file");
        }
    }

}
