package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class RejectPostRequest {

    @JsonProperty("deny_message")
    private String denyMessage;

}
