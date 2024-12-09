package com.example.peaceful_land.Exception;

public class RequestInvalidException extends RuntimeException {
    public RequestInvalidException() {
        super("yêu cầu không hợp lệ");
    }
}
