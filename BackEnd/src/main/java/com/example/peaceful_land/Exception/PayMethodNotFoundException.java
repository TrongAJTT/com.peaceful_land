package com.example.peaceful_land.Exception;

public class PayMethodNotFoundException extends RuntimeException {
    public PayMethodNotFoundException() {
        super("Phương thức thanh toán không tồn tại");
    }
}
