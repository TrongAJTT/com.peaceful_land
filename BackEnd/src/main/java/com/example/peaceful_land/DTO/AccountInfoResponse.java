package com.example.peaceful_land.DTO;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Utils.VariableUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter @Builder
public class AccountInfoResponse {

    private String name;
    private String role;
    private String email;
    private String phone;
    private Long balance;
    @JsonProperty("birth_day")
    private String birthDay;

    public static AccountInfoResponse from(Account account){
        return AccountInfoResponse.builder()
                .name(account.getName())
                .role(account.getRoleString())
                .email(account.getEmail())
                .phone(account.getPhone())
                .balance(account.getAccountBalance())
                .birthDay(account.getBirthDate().format(VariableUtils.FORMATTER_DATE))
                .build();
    }

}
