package com.example.peaceful_land.Exception;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException() {
        super("Không tìm thấy bất động sản");
    }
}
