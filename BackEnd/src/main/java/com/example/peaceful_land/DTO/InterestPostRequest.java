package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class InterestPostRequest {

    private Long postId;

    @JsonProperty("user_id")
    private Long userId;

    private Integer interested;

    // Chỉ khi interested = 1 thì mới có thể set notification
    private Integer notification;

    public Boolean isInterested(){
        return interested == 1;
    }

    public Boolean isNotification(){
        return interested == 1 && notification == 1;
    }

}
