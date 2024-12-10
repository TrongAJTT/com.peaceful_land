package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data @Getter @Setter @Builder
public class TourRequest {

    private Byte type;   // 1 : directly tour, 0: homestay video call

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("expected_date")
    private LocalDate expectedDate;

    @JsonProperty("expected_hour")
    private Byte hour;

    private String name;

    private String phone;

    private String email;

    @JsonProperty("interest_level")
    private Byte interestLevel; // 1: high, 0: low

}