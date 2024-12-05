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
            throw new IllegalArgumentException("tên tập tin chứa ký tự không hợp lệ");
        }
        // Kiểm tra tên file rỗng
        if (fileName.isBlank()) {
            throw new IllegalArgumentException("Tên tập tin rỗng");
        }
        // Tạo tên file mới để tránh trùng lặp
        String uniqueFileName = "";
        if (uploadType == VariableUtils.TYPE_UPLOAD_AVATAR) {
            uniqueFileName = "avatars/" + UUID.randomUUID() + "_" + fileName;
        } else if (uploadType == VariableUtils.TYPE_UPLOAD_PROPERTY_IMAGE) {
            uniqueFileName = "property_imgs/" + UUID.randomUUID() + "_" + fileName;
        } else if (uploadType == VariableUtils.TYPE_UPLOAD_POST_THUMBNAIL) {
            uniqueFileName = "post_thumbns/" + UUID.randomUUID() + "_" + fileName;
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

    public static void checkImageFile(MultipartFile file) {
        // Kiểm tra file rỗng
        if (file == null) {
            throw new RuntimeException("Tập tin không tồn tại");
        }
        // Kiểm tra kích thước file (nax 5MB)
        if(file.getSize() > 5*1024*1024) {
            throw new RuntimeException("Tập tin quá lớn. Kích thước tối đa là 5MB");
        }
        // Kiểm tra content type
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")){
            throw new RuntimeException("Tập tin không phải là hình ảnh");
        }
    }
}
