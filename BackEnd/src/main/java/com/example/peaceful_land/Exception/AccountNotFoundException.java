package com.example.peaceful_land.Exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("Tài khoản không tồn tại");
    }
}
