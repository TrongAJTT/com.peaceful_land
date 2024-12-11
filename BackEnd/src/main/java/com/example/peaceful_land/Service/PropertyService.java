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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static com.example.peaceful_land.Utils.VariableUtils.TYPE_UPLOAD_PROPERTY_IMAGE;

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
        // Tạo thư mục upload nếu chưa tồn tại
        try {
            ImageUtils.createUploadDirIfNotExists(TYPE_UPLOAD_PROPERTY_IMAGE);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo thư mục upload: " + e.getMessage());
        }
        // Lấy danh sách file ảnh
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

    @Override
    public List<String> getImages(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(PropertyNotFoundException::new);
        return propertyImageRepository.findAllByPropertyEquals(property)
                .stream().map(PropertyImage::getFileUrl).toList();
    }

    @Transactional
    public void SYSTEM_scanAndDeleteUnusedImages() {
        new Thread(() -> {
            List<String> listImg = propertyImageRepository.findAllThumbnails().stream()
                    .filter(thum -> !thum.equals(VariableUtils.IMAGE_NA)).toList();
            Path uploadDir = Path.of(VariableUtils.UPLOAD_DIR_PROP_IMG);
            try {
                // Lấy danh sách tất cả các tệp trong thư mục uploads/avatars
                List<Path> allFiles = Files.walk(uploadDir)
                        .filter(Files::isRegularFile) // Chỉ lấy các tệp, không lấy thư mục
                        .toList();

                // Xóa tệp trên server nếu không nằm trong listAvatars
                for (Path file : allFiles) {
                    String fileName = file.getFileName().toString();
                    // Kiểm tra xem tệp có nằm trong listAvatars không
                    if (!listImg.contains(VariableUtils.UPLOAD_DIR_PROP_POSTFIX + fileName)) {
                        // Xóa tệp nếu không nằm trong danh sách
                        Files.delete(file);
                        System.out.println(VariableUtils.getServerScanPrefix() + "Delete unused property images on server storage " + file);
                    }
                }

                // Xóa tệp trong database nếu không nằm trong server
                for (String thumb : listImg) {
                    Path thumbPath = Path.of(uploadDir.toString(), thumb.split("/")[1]);
                    if (!Files.exists(thumbPath)) {
                        System.out.println(thumb);
                        Optional<PropertyImage> propertyImage = propertyImageRepository.findByFileUrl(thumb);
                        propertyImage.ifPresent(propertyImageRepository::delete);
                        System.out.println(VariableUtils.getServerScanPrefix() + "Delete unused property images on database " + thumbPath);
                    }
                }

                System.out.println(">>>\n" + VariableUtils.getServerScanPrefix() + "Scan and delete unused property images completed\n<<<");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
