package com.example.peaceful_land.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data @Getter @Setter
public class ChangeAvatarRequest {
    private Long userId;
    private MultipartFile image;
}
