package com.example.peaceful_land.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ReportRequest {
    private Byte object;
    private String reason;
    private String description;
    private String name;
    private String phone;
    private String email;
}
