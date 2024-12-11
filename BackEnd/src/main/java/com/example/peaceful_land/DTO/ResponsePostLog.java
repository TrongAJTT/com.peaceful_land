package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ResponsePostLog {
    @JsonProperty("created_at")
    private String createdAt;
    private String title;
    private String description;
    private String thumbnail_url;
}
