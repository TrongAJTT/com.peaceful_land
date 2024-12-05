package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class PurchaseRoleRequest {

    public static final Byte ROLE_BROKER = 1;
    public static final Byte ROLE_BROKER_VIP = 2;
    public static final Integer DAY_30 = 30;
    public static final Integer DAY_90 = 90;
    public static final Integer DAY_180 = 180;
    public static final Integer DAY_360 = 360;

    @JsonProperty("user_id")
    private Long userId;
    private Byte role;
    private Integer day;

}
