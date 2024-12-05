package com.example.peaceful_land.Utils;

import java.util.Objects;

public class PriceUtils {

    public static final Byte ROLE_BROKER = 1;
    public static final Byte ROLE_BROKER_VIP = 2;

    public static Long getRolePriceFromDayRange(Byte role, Integer day) {
        if (Objects.equals(role, ROLE_BROKER)) {
            if (day == 30) {
                return 99000L;
            } else if (day == 90) {
                return 280000L;
            } else if (day == 180) {
                return 475000L;
            } else if (day == 360) {
                return 830000L;
            }
        } else if (Objects.equals(role, ROLE_BROKER_VIP)) {
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

}
