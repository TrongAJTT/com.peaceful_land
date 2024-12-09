package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data @Getter @Setter @Builder
public class UpdateAccountInfoRequest {

    @JsonProperty("user_id")
    private Long userId;
    private String name;
    private String email;
    private String phone;
    @JsonProperty("birth_date")
    private LocalDate birthDate;

}
