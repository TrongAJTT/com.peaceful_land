package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ExtendPostRequest {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("day_expand")
    private Byte dayExpand;
}
