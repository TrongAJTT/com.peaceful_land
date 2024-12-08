package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AccountPrimaryInfo {
    private Long id;

    private String email;

    private String password;

    private String name;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    private String phone;
}
