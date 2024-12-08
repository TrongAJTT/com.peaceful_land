package com.example.peaceful_land.Utils;

import java.util.Objects;

public class VariableUtils {

    public static final Byte ROLE_NORMAL        = 0;
    public static final Byte ROLE_BROKER        = 1;
    public static final Byte ROLE_BROKER_VIP    = 2;
    public static final Byte ROLE_ADMIN         = 3;

    public static final String ROLE_NORMAL_STR      = "Người dùng thông thường";
    public static final String ROLE_BROKER_STR      = "Môi giới";
    public static final String ROLE_BROKER_VIP_STR  = "Môi giới VIP";
    public static final String ROLE_ADMIN_STR       = "Quản trị viên";

    public static final String DEFAULT_AVATAR = "template/blank_avatar.webp";
    public static final String IMAGE_NA = "template/image_state_not_available.jpg";

    public static final int TYPE_UPLOAD_AVATAR = 1;
    public static final int TYPE_UPLOAD_PROPERTY_IMAGE = 2;
    public static final int TYPE_UPLOAD_POST_THUMBNAIL = 3;

    public static final String PURCHASE_ACTION_BROKER = "Mua gói môi giới";
    public static final String PURCHASE_ACTION_BROKER_VIP = "Mua gói môi giới VIP";
    public static final String PURCHASE_ACTION_EXTEND_ROLE = "Gia hạn gói";
    public static final String PURCHASE_ACTION_EXTEND_POST = "Gia hạn bài đăng";

    public static final int MAX_POST_NORMAL_TOTAL = 5;
    public static final int MAX_POST_NORMAL_PER_DAY = 1;
    public static final int MAX_POST_BROKER_PER_DAY = 5;
    public static final int MAX_POST_BROKER_VIP_PER_DAY = 10;

    public static final String
            UPDATE_TYPE_SOLD = "sold",
            UPDATE_TYPE_RENTED = "rented",
            UPDATE_TYPE_RESALE = "re-sale",
            UPDATE_TYPE_RERENT = "re-rent",
            UPDATE_TYPE_PRICE = "price",
            UPDATE_TYPE_OFFER = "offer",
            UPDATE_TYPE_RENTAL_PERIOD = "rental-period",
            UPDATE_TYPE_DISCOUNT = "discount",
            UPDATE_TYPE_POST = "post";

    public static Long getRolePriceFromDayRange(Byte roleId, Integer day) {
        if (Objects.equals(roleId, ROLE_BROKER)) {
            if (day == 30) {
                return 99000L;
            } else if (day == 90) {
                return 280000L;
            } else if (day == 180) {
                return 475000L;
            } else if (day == 360) {
                return 830000L;
            }
        } else if (Objects.equals(roleId, ROLE_BROKER_VIP)) {
            if (day == 30) {
                return 154000L;
            } else if (day == 90) {
                return 440000L;
            } else if (day == 180) {
                return 740000L;
            } else if (day == 360) {
                return 1290000L;
            }
        }
        return -1L;
    }

    public static String getRoleString(Byte roleId){
        return switch (roleId) {
            case 0 -> ROLE_NORMAL_STR;
            case 1 -> ROLE_BROKER_STR;
            case 2 -> ROLE_BROKER_VIP_STR;
            default -> ROLE_ADMIN_STR;
        };
    }

    public static Integer getPostLiveTimeDay(Byte roleId) {
        return switch (roleId) {
            case 0 -> 7;
            case 1 -> 10;
            default -> 14;
        };
    }

    public static Integer getApprovalDayRange(Byte roleId) {
        return switch (roleId){
            case 0 -> 2;
            case 1 -> 1;
            case 2 -> 1;
            default -> -1;
        };
    }

    public static Integer getPostLimitPerDay(Byte roleId){
        return switch (roleId) {
            case 0 -> MAX_POST_NORMAL_PER_DAY;
            case 1 -> MAX_POST_BROKER_PER_DAY;
            default -> MAX_POST_BROKER_VIP_PER_DAY;
        };
    }

    public static String getUpdateActionString(String actionUpdateId){
        return switch (actionUpdateId) {
            case UPDATE_TYPE_SOLD -> "Đã bán";
            case UPDATE_TYPE_RENTED -> "Đã bán', 'N";
            case UPDATE_TYPE_RESALE -> "Người mua bán lại";
            case UPDATE_TYPE_RERENT -> "Hết hợp đồng, cho thuê lại";
            case UPDATE_TYPE_PRICE -> "Cập nhật giá";
            case UPDATE_TYPE_OFFER -> "Thay đổi hình thức";
            case UPDATE_TYPE_RENTAL_PERIOD -> "Thay đổi hạn cho thuê";
            case UPDATE_TYPE_DISCOUNT -> "Giảm giá";
            default -> "Cập nhật tin rao";
        };
    }

}
