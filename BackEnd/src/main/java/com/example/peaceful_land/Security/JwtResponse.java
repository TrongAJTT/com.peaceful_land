package com.example.peaceful_land.Security;

import com.example.peaceful_land.Entity.Account;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JwtResponse {

    private String token;
    private Account account;

    public JwtResponse(String token,Account account) {
        this.token = token;
        this.account = account;
    }
}
