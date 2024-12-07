package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Getter @Setter @Builder
public class ResponseUserPostReqView {
    private Long id;
    private ResponsePost data;
    private LocalDate expiration;
    private Boolean approved;

    @JsonProperty("deny_message")
    private String denyMessage;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

}