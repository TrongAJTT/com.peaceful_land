package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PropertyImagesRequest;
import com.example.peaceful_land.DTO.PropertyRequest;
import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Entity.PropertyImage;
import com.example.peaceful_land.Exception.PropertyNotFoundException;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Repository.PropertyImageRepository;
import com.example.peaceful_land.Repository.PropertyLogRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import com.example.peaceful_land.Utils.ImageUtils;
import com.example.peaceful_land.Utils.VariableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service @RequiredArgsConstructor
public class PropertyService implements IPropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyLogRepository propertyLogRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final AccountRepository accountRepository;

    @Override
    public Property createProperty(PropertyRequest propertyRequest) {
        try {
            Account account = accountRepository.findById(propertyRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            // Lấy đối tượng bất động sản ánh xạ
            Property newProperty = propertyRequest.parsePropertyWithoutAccount();
            // Gán tài khoản sở hữu
            newProperty.setUser(account);
            // Lưu vào cơ sở dữ liệu
            newProperty = propertyRepository.save(newProperty);
            // Chuyển sang trạng thái ẩn (chờ duyệt)
            newProperty.setHide(true);
            propertyRepository.save(newProperty);
            // Lưu vào nhật ký thay đổi
            propertyLogRepository.save(newProperty.toPropertyLog());
           // Trả về bất động sản mới
            return newProperty;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String uploadImages(PropertyImagesRequest request) {
        if (request.getProperty_id() == null) {
            throw new IllegalArgumentException("ID bất động sản không được để trống");
        }
        Property property = propertyRepository.findById(request.getProperty_id())
                .orElse(null);
        if (property == null) {
            throw new PropertyNotFoundException();
        }
        List<MultipartFile> files = request.getImages();
        StringBuilder log = new StringBuilder();
        int successCount = 0;
        for(int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            // Lưu file vào server
            try {
                // Kiểm tra file ảnh
                ImageUtils.checkImageFile(file);
                // Lưu file vào server
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
            catch (Exception e) {
                log.append("Lỗi khi lưu tập tin thứ ")
                        .append(i + 1).append(": ").append(e.getMessage())
                        .append("\n");
            }
        }
        if (successCount == 0) {
            throw new RuntimeException("Không có tập tin nào được lưu:\n" + log);
        }
        else if (successCount < files.size()) {
            return String.format("Lưu (%d/%d) tập tin thành công:\n%s", successCount, files.size(), log);
        }
        return "Lưu tập tin thành công";
    }

}
