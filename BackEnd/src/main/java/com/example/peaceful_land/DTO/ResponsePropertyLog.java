package com.example.peaceful_land.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data @Getter @Setter @Builder
public class ResponsePropertyLog {
    private LocalDateTime date;
    private String action;
    private Long price;
}