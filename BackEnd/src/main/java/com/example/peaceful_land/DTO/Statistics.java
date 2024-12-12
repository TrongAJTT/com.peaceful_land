package com.example.peaceful_land.DTO;

import lombok.*;

@Builder @Getter @Setter @Data
@NoArgsConstructor @AllArgsConstructor
public class Statistics {
    private String name;
    private Long thisWeek;
    private Long past7Days;
    private Long thisMonth;
    private Long past30Days;
    private Long thisYear;
    private Long past365Days;
    private Long allTime;
}
