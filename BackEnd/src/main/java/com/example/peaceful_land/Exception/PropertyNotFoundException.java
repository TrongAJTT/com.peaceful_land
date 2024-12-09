package com.example.peaceful_land.Exception;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException() {
        super("Bất động sản không tồn tại hoặc đã bị xóa");
    }
}
