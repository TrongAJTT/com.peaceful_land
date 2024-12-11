package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ResponseUserPostReqView {
    private Long id;
    private ResponsePost data;
    private String expiration;
    private Boolean approved;

    @JsonProperty("deny_message")
    private String denyMessage;

    @JsonProperty("created_at")
    private String createdAt;

}