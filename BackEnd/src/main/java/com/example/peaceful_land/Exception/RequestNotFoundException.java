package com.example.peaceful_land.Exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException() {
        super("Không tìm thấy yêu cầu");
    }
}
