package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data @Getter @Setter @Builder
public class ResponsePostLog {
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private String thumbnail_url;
}
