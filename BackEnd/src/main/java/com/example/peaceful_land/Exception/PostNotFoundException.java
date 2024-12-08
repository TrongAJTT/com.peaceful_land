package com.example.peaceful_land.Exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("Bài rao không tồn tại hoặc đã bị xóa");
    }
}
