package com.example.peaceful_land.Controller;

import com.example.peaceful_land.Entity.File;
import com.example.peaceful_land.Service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/files")
public class FileController {

    private static final int TYPE_UPLOAD_AVATAR = 1;

    private final FileService fileService;

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(MultipartFile file) {
        if (file== null){
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
            String fileName = saveFileServer(file, TYPE_UPLOAD_AVATAR);
            File dbFile = fileService.createFile(fileName);
            return ResponseEntity.ok("File uploaded successfully with id: " + dbFile.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while saving file");
        }
    }

    private String saveFileServer(MultipartFile file, int uploadType) throws IOException {
        String contentType = file.getContentType();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Kiểm tra tên file
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("File name contains invalid path sequence " + fileName);
        }
        // Kiểm tra tên file rỗng
        if (fileName.isBlank()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // Tạo tên file mới để tránh trùng lặp
        String uniqueFileName = "";
        if (uploadType == TYPE_UPLOAD_AVATAR) {
            uniqueFileName = "avatars/" + UUID.randomUUID() + "_" + fileName;
        }

        // Tạo đường dẫn lưu file nếu chưa tồn tại
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Lấy đường dẫn đầy đủ đến file
        Path filePath = Paths.get(uploadDir.toString(), uniqueFileName);
        // Lưu file vào thư mục uploads
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

}
