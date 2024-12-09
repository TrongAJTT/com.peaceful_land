package com.example.peaceful_land.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class UpdateAccountInfoRequest {
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String dateOfBirth;
//    private
}
