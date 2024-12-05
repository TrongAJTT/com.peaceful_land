package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data @Getter @Setter
public class ChangeAvatarRequest {

    @JsonProperty("user_id")
    private Long userId;

    private MultipartFile image;

}
