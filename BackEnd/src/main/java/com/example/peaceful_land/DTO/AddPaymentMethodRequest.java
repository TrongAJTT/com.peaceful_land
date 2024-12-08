package com.example.peaceful_land.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class AddPaymentMethodRequest {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("is_wallet")
    private Boolean isWallet;

    private String name;

    @JsonProperty("account_number")
    private String accountNumber;
}
