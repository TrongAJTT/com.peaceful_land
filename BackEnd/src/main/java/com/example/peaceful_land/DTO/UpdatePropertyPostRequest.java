package com.example.peaceful_land.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data @Getter @Setter
public class UpdatePropertyPostRequest {

    private Long user_id;

    private Long post_id;

    private String action;

    // Cập nhật liên quan đến bài rao

    private String title;

    private String description;

    private MultipartFile thumbnail;

    // Cập nhật liên quan đến bất động sản

    private Boolean offer;

    private Boolean property_status;

    private LocalDate rental_period;

    private Long price;

}
