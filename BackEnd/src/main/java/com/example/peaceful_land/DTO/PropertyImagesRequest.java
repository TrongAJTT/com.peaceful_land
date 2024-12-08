package com.example.peaceful_land.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data @Getter @Setter
public class PropertyImagesRequest {
    private Long property_id;
    private List<MultipartFile> images;
}
