package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.PropertyImagesRequest;
import com.example.peaceful_land.DTO.PropertyRequest;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.PropertyImage;
import com.example.peaceful_land.Repository.PropertyImageRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import com.example.peaceful_land.Service.IPropertyService;
import com.example.peaceful_land.Utils.ImageUtils;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/properties")
public class PropertyController {

    private final IPropertyService propertyService;
    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;

    @PostMapping("/create-property")
    public ResponseEntity<?> createProperty(@RequestBody PropertyRequest request) {
        try {
            Property newProperty = propertyService.createProperty(request);
            return ResponseEntity.ok("Property created successfully:\n" + newProperty);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute PropertyImagesRequest request) {
        if (request.getProperty_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID bất động sản không được để trống");
        }
        Property property = propertyRepository.findById(request.getProperty_id())
                .orElse(null);
        if (property == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bất động sản");
        }
        List<MultipartFile> files = request.getImages();
        StringBuilder log = new StringBuilder();
        int successCount = 0;
        for(int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            // Kiểm tra file rỗng
            if (file.getName().isEmpty()) {
                log.append("Tập tin thứ ")
                        .append(i + 1)
                        .append(" không tồn tại\n");
                continue;
            }
            // Kiểm tra kích thước file (nax 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                log.append("Tập tin thứ ")
                        .append(i + 1).append(" quá lớn (")
                        .append(file.getSize() / 1024 / 1024)
                        .append("MB). Kích thước tối đa là 5MB\n");
                continue;
            }
            // Kiểm tra content type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                log.append("Tập tin thứ ")
                        .append(i + 1)
                        .append(" không phải là hình ảnh\n");
                continue;
            }
            // Lưu file vào server
            try {
                String fileName = ImageUtils.saveFileServer(file, VariableUtils.TYPE_UPLOAD_PROPERTY_IMAGE);
                // Cập nhật đường dẫn file mới vào database
                propertyImageRepository.save(
                        PropertyImage.builder()
                                .property(property)
                                .fileUrl(fileName)
                                .build()
                );
                log.append("Tập tin thứ ")
                        .append(i + 1)
                        .append(" đã được lưu\n");
                successCount++;
            }
            catch (IOException e) {
                log.append("Lỗi khi lưu tập tin thứ ")
                        .append(i + 1).append(": ").append(e.getMessage())
                        .append("\n");
            }
        }
        if (successCount == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lôi khi lưu tập tin:\n" + log);
        }
        else if (successCount < files.size()) {
            return ResponseEntity.ok(
                    String.format("Lưu (%d/%d) tập tin thành công:\n%s", successCount, files.size(), log)
            );
        }
        return ResponseEntity.ok("Lưu tập tin thành công");
    }

}
