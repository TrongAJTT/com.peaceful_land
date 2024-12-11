package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class ResponseReport {
    private Long id;
    @JsonProperty("property_id")
    private Long propertyId;
    private String object;
    private String reasons;
    private String description;
    private String name;
    private String phone;
    private String email;
    @JsonProperty("is_handled")
    private Boolean isHandled;
}
