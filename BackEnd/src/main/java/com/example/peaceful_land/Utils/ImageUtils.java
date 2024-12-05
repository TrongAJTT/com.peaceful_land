package com.example.peaceful_land.Utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

public class ImageUtils {

    public static String saveFileServer(MultipartFile file, int uploadType) throws IOException {
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
        if (uploadType == VariableUtils.TYPE_UPLOAD_AVATAR) {
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

    public static void deleteFileServer(String fileName) throws IOException {
        Path uploadDir = Paths.get("uploads");
        Path filePath = Paths.get(uploadDir.toString(), fileName);
        Files.deleteIfExists(filePath);
    }
}
