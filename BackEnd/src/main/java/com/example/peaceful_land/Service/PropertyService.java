package com.example.peaceful_land.Service;

import com.example.peaceful_land.DTO.PropertyRequest;
import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.Property;
import com.example.peaceful_land.Repository.AccountRepository;
import com.example.peaceful_land.Repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class PropertyService implements IPropertyService {

    private final PropertyRepository propertyRepository;
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
            // Chuyển sang trạng thái ẩn (chờ duyệt)
            newProperty.setHide(true);
            return propertyRepository.save(newProperty);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
