package com.example.peaceful_land.Service;

import com.example.peaceful_land.Model.Account;
import com.example.peaceful_land.Model.LoginRequest;
import com.example.peaceful_land.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AccountRepository accountRepository;

    public Account tryLogin(LoginRequest loginRequest) {
        // Xử lý ngoại lệ

        // Xử lý và trả về Account nếu hợp lệ
        return new Account();
    }

}
