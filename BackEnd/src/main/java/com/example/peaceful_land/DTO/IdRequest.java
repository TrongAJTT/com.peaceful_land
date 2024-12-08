package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Post;
import com.example.peaceful_land.Entity.Property;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class IdRequest {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("payment_method_id")
    private Long paymentMethodId;

    private Post post;

    private Property property;
}