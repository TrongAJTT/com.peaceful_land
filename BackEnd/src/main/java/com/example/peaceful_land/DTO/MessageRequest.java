package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class MessageRequest {
    @JsonProperty("deny_message")
    private String denyMessage;

    @JsonProperty("reply_message")
    private String replyMessage;

}
