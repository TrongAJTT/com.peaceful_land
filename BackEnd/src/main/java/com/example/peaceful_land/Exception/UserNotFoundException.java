package com.example.peaceful_land.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Người dùng không tồn tại hoặc đã bị xóa");
    }
}
