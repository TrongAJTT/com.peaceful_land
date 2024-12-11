package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.PaymentMethod;
import com.example.peaceful_land.Utils.VariableUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class PaymentMethodResponse {

    private Long id;

    @JsonProperty("is_wallet")
    private Boolean isWallet;

    private String name;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("created_at")
    private String createdAt;

    public static PaymentMethodResponse from(PaymentMethod method){
        return PaymentMethodResponse.builder()
                .id(method.getId())
                .isWallet(method.getIsWallet())
                .name(method.getName())
                .accountNumber(method.getAccountNumber())
                .createdAt(method.getDateBegin().format(VariableUtils.FORMATTER_DATE_TIME))
                .build();
    }

}
