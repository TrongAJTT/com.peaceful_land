package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ResponseReqTour {

    private String type;

    @JsonProperty("expected_time")
    private String expectedTime;
    private String name;
    private String phone;
    private String email;

    @JsonProperty("interest_level")
    private String interestLevel;

    @JsonProperty("created_at")
    private String createdAt;

}
