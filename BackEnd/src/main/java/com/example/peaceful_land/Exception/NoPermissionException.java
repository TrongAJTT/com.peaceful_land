package com.example.peaceful_land.Exception;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super("Bạn không có quyền thực hiện chức năng này");
    }
}
