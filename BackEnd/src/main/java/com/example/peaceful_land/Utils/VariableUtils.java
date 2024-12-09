package com.example.peaceful_land.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    public static final String DEFAULT_UPLOAD_DIR = "uploads";

    public static final int TYPE_UPLOAD_AVATAR = 1;
    public static final int TYPE_UPLOAD_PROPERTY_IMAGE = 2;
    public static final int TYPE_UPLOAD_POST_THUMBNAIL = 3;
    public static final String TYPE_UPLOAD_AVATAR_PATH = "avatars";
    public static final String TYPE_UPLOAD_PROPERTY_IMAGE_PATH = "property_imgs";
    public static final String TYPE_UPLOAD_POST_THUMBNAIL_PATH = "post_thumbns";

    public static final String PURCHASE_ACTION_BROKER = "Mua gói môi giới";
    public static final String PURCHASE_ACTION_BROKER_VIP = "Mua gói môi giới VIP";
    public static final String PURCHASE_ACTION_EXTEND_ROLE = "Gia hạn gói";
    public static final String PURCHASE_ACTION_EXTEND_POST = "Gia hạn bài đăng";
    public static final String PURCHASE_ACTION_WITHDRAW = "Rút tiền";

    public static final String REQUEST_STATE_ALL = "all";
    public static final String REQUEST_STATE_PENDING = "pending";
    public static final String REQUEST_STATE_APPROVED = "approved";
    public static final String REQUEST_STATE_REJECTED = "rejected";

    public static final String REQUEST_INTEREST_INFO = "Tôi muốn biết thêm thông tin";
    public static final String REQUEST_INTEREST_BUY = "Tôi muốn mua / thuê bất động sản này";

    public static final String REQUEST_TOUR_DIRECT = "'Xem trực tiếp'";
    public static final String REQUEST_TOUR_HOMESTAY = "Trò chuyện video online";

    public static final int MAX_POST_NORMAL_TOTAL = 5;
    public static final int MAX_POST_NORMAL_PER_DAY = 1;
    public static final int MAX_POST_BROKER_PER_DAY = 5;
    public static final int MAX_POST_BROKER_VIP_PER_DAY = 10;

    public static final int REQUEST_REPORT_OBJECT_ID_POST = 0;
    public static final int REQUEST_REPORT_OBJECT_ID_MAP = 1;
    public static final String REQUEST_REPORT_OBJECT_VI_POST = "Tin rao";
    public static final String REQUEST_REPORT_OBJECT_VI_MAP = "Bản đồ";

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

    public static final String REQUEST_TYPE_CONTACT = "contact";
    public static final String REQUEST_TYPE_TOUR = "tour";

    public static String getStringFromUploadType(int uploadType) {
        return switch (uploadType) {
            case TYPE_UPLOAD_AVATAR -> TYPE_UPLOAD_AVATAR_PATH;
            case TYPE_UPLOAD_PROPERTY_IMAGE -> TYPE_UPLOAD_PROPERTY_IMAGE_PATH;
            case TYPE_UPLOAD_POST_THUMBNAIL -> TYPE_UPLOAD_POST_THUMBNAIL_PATH;
            default -> "";
        };
    }

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
            case 1, 2 -> 1;
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
            case UPDATE_TYPE_RENTED -> "Đã cho thuê";
            case UPDATE_TYPE_RESALE -> "Người mua bán lại";
            case UPDATE_TYPE_RERENT -> "Hết hợp đồng, cho thuê lại";
            case UPDATE_TYPE_PRICE -> "Cập nhật giá";
            case UPDATE_TYPE_OFFER -> "Thay đổi hình thức";
            case UPDATE_TYPE_RENTAL_PERIOD -> "Thay đổi hạn cho thuê";
            case UPDATE_TYPE_DISCOUNT -> "Giảm giá";
            default -> "Cập nhật tin rao";
        };
    }

    public static String convertToVnTimeZoneString(LocalDateTime localDateTime) {
        // Chuyển LocalDateTime sang ZonedDateTime với múi giờ cụ thể
        ZonedDateTime zonedCreatedAt = localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        // Định dạng thời gian thành chuỗi mong muốn
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return zonedCreatedAt.format(formatter);
    }

}
