package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Utils.VariableUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data @Getter @Setter
public class PostRequest {

    @JsonProperty("property_id")
    private Long propertyId;

    private String title;

    private String description;

    private Integer expiration; // Số ngày hết hạn sau đó mà người dùng thiết lập

    public Post parsePostWithoutProperty(){
        try {
            return Post.builder()
                    .title(this.title)
                    .description(this.description)
                    .expiration(LocalDate.now().plusDays(expiration))
                    .thumbnUrl(VariableUtils.IMAGE_NA)
                    .status(true)
                    .build();
        } catch (Exception e){
            return null;
        }
    }

}
