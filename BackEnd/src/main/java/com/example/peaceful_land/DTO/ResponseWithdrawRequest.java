package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ResponseWithdrawRequest {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String status;

    @JsonProperty("deny_message")
    private String denyMessage;

    @JsonProperty("request_date")
    private String requestDate;

}
