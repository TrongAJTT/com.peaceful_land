package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class ResetPasswordRequest {
    private String email;
    private String otp;
    @JsonProperty("new_password")
    private String newPassword;
}
