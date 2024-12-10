package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ContactRequest {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private String phone;

    private String email;

    @JsonProperty("interest_level")
    private Byte interestLevel; // 1: high, 0: low

    private String message;
}