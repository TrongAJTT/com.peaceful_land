package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Getter @Setter @Builder
public class ResponsePost {

    private Long id;
    private ResponseProperty property;
    private String title;
    private Boolean status;
    private String description;

    @Column(name = "thumbn_url")
    private String thumbnUrl;

    private LocalDate expiration;
    private LocalDateTime createdAt;

    public static ResponsePost fromPost(Post post) {
        return ResponsePost.builder()
                .id(post.getId())
                .property(ResponseProperty.fromProperty(post.getProperty()))
                .title(post.getTitle())
                .status(post.getStatus())
                .description(post.getDescription())
                .thumbnUrl(post.getThumbnUrl())
                .expiration(post.getExpiration())
                .createdAt(post.getDateBegin())
                .build();
    }

}
