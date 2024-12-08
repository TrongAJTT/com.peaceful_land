package com.example.peaceful_land.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDate rental_period;

    private Long price;

    // Cập nhật liên quan đến giảm giá bất động sản

    private Long discount_price;

    private LocalDateTime discount_expiration;

}
