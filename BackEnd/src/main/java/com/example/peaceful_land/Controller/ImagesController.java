package com.example.peaceful_land.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/images")
public class ImagesController {

    @GetMapping()
    public ResponseEntity<Resource> getImage(@RequestParam String path) {
        try {
            Path uploadDir = Paths.get("uploads/");
            Path filePath = Paths.get(uploadDir.toString(), path);
            System.out.println(filePath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Xác định Content-Type từ phần mở rộng tệp
                String contentType = Files.probeContentType(filePath);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/base64")
    public ResponseEntity<String> getImageBase64(@RequestParam String path) {
        try {
            // Đường dẫn thư mục uploads
            Path uploadDir = Paths.get("uploads/");
            Path filePath = Paths.get(uploadDir.toString(), path);

            // Kiểm tra file tồn tại và có thể đọc được
            if (Files.exists(filePath) && Files.isReadable(filePath)) {
                // Đọc toàn bộ nội dung tệp thành byte[]
                byte[] fileContent = Files.readAllBytes(filePath);

                // Encode nội dung file sang Base64
                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                // Trả về Base64 trong response
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN) // Định dạng chuỗi text
                        .body(base64Content);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found or unreadable.");
            }
        } catch (Exception e) {
            // Xử lý lỗi và trả về status 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading file: " + e.getMessage());
        }
    }

}
