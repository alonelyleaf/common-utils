package com.alonelyleaf.time.dst.util;

import com.alonelyleaf.time.constant.Constant;
import jodd.util.StringPool;

import java.util.TimeZone;

/**
 * @author ChNan
 */
public class TimeZoneUtil {

    private static final String GMT = "GMT";

    /**
     * 获得UTC零时区
     *
     * @return
     */
    public static TimeZone getZeroTimeZone() {

        return TimeZone.getTimeZone(Constant.DateTimeOperator.TIME_ZONE_UTC_ZERO);
    }

    public static TimeZone getTimeZone(int utcOffset) {

        return TimeZone.getTimeZone(generateGMTFormat(utcOffset));
    }

    /**
     * 添加时区偏移量
     *
     * @return 时区的GMT表现形式，不带夏令时，例如 GMT+08:00
     */
    public static String generateGMTFormat(int offset) {

        return generateTimeZone(findPositiveOrNegative(offset),
                findHour(Math.abs(offset)),
                findMinute(Math.abs(offset))
        );
    }

    /**
     * 添加时区偏移量
     *
     * @return 时区的GMT表现形式，不带夏令时，例如 +08:00
     */
    public static String generateOffsetDisplay(int offset) {
        return generateOffset(findPositiveOrNegative(offset),
                findHour(Math.abs(offset)),
                findMinute(Math.abs(offset))
        );
    }

    public static String generateOffset(String positiveOrNegative, String hour, String minute) {
        return positiveOrNegative + hour + StringPool.COLON + minute;
    }

    private static String findPositiveOrNegative(int offset) {
        return offset >= 0 ? StringPool.PLUS : StringPool.DASH;
    }

    private static String findHour(int absOffset) {
        String hour = String.valueOf(absOffset / 3600); //将偏移量转化为小时（小数去除不要）
        if (hour.length() == 1) {
            hour = StringPool.ZERO + hour;
        }
        return hour;
    }

    private static String findMinute(int absOffset) {
        String sDecimalsZone = String.valueOf((absOffset % 3600) / 60); // 取出分钟数
        if (sDecimalsZone.length() == 1) {
            sDecimalsZone = sDecimalsZone + StringPool.ZERO;
        } else if (sDecimalsZone.length() > 3) {
            sDecimalsZone = sDecimalsZone.substring(0, 2);
        }
        return sDecimalsZone;
    }

    private static String generateTimeZone(String positiveOrNegative, String hour, String minute) {
        return GMT + generateOffset(positiveOrNegative, hour, minute);
    }
}
