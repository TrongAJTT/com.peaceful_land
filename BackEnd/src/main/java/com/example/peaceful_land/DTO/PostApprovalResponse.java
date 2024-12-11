package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data @Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PostApprovalResponse {
    private Long id;

    @JsonProperty("request_date")
    private String requestDate;

    @JsonProperty("expiry_date")
    private String expiryDate;

    private Boolean approved;

    @JsonProperty("deny_message")
    private String denyMessage;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("post_title")
    private String postTitle;

    @JsonProperty("post_visibility")
    private Boolean postVisibility;

    @JsonProperty("property_address")
    private String propertyAddress;

}
