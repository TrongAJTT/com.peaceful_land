package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Utils.VariableUtils;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ResponsePost {

    private Long id;
    private ResponseProperty property;
    private String title;
    private Boolean status;
    private String description;

    @Column(name = "thumbn_url")
    private String thumbnUrl;

    private String expiration;
    private String createdAt;

    public static ResponsePost fromPost(Post post) {
        return ResponsePost.builder()
                .id(post.getId())
                .property(ResponseProperty.fromProperty(post.getProperty()))
                .title(post.getTitle())
                .status(post.getStatus())
                .description(post.getDescription())
                .thumbnUrl(post.getThumbnUrl())
                .expiration(post.getExpiration().format(VariableUtils.FORMATTER_DATE))
                .createdAt(post.getDateBegin().format(VariableUtils.FORMATTER_DATE_TIME))
                .build();
    }

}
