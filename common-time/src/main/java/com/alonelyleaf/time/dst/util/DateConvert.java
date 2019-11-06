package com.alonelyleaf.time.dst.util;

import com.alonelyleaf.time.constant.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *         java.util.Date代表一个时间点，其值为距公元1970年1月1日 00:00:00的毫秒数。所以它是没有时区和Locale概念的
 */
public class DateConvert {

    private DateConvert() {
    }

    /**
     * 根据字符串时间获取时间
     *
     * @param date          字符串时间
     * @param formatPattern 时间格式
     * @param offset        所在时区偏移，单位：s
     * @return
     */
    public static Date toDate(String date,
                              String formatPattern,
                              int offset) {
        return toDate(date, formatPattern, TimeZoneUtil.getTimeZone(offset));
    }

    /**
     * 根据字符串时间获取时间
     *
     * @param date          字符串时间
     * @param formatPattern 时间格式
     * @param timeZone      所在时区偏移
     * @return
     */
    public static Date toDate(String date,
                              String formatPattern,
                              TimeZone timeZone) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern);
            if (timeZone != null) {
                format.setTimeZone(timeZone);
            }
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date toDateTimeNoSecond(String date,
                                          int offset) {
        return toDate(date, Constant.DateTimeFormat.DATETIME_NO_SECONDS, offset);
    }

    public static Date toDefaultDate(String date,
                                     TimeZone timeZone) {
        return toDate(date, Constant.DateTimeFormat.DATE, timeZone);
    }

    public static Date toDateTimeWithSecond(String date,
                                            TimeZone timeZone) {
        return toDate(date, Constant.DateTimeFormat.DATETIME_WITH_SECONDS, timeZone);
    }

    public static Date toDateTimeWithSecond(String date,
                                            int offset) {
        return toDate(date, Constant.DateTimeFormat.DATETIME_WITH_SECONDS, offset);
    }

    public static Date toDateTimeWithSecond(String date) {
        return toDate(date, Constant.DateTimeFormat.DATETIME_WITH_SECONDS);
    }

    public static Date toDate(String date,
                              String formatPattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern);
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String toString(Date date,
                                  String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String toString(Date date,
                                  String format,
                                  Integer offset) {
        return toString(date, format, TimeZone.getTimeZone(DateUtil.appendZoneSuffix(offset)));
    }

    public static String toString(Date date,
                                  String format,
                                  TimeZone timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat.format(date);
    }

    public static Date toDate(Long date, int uctOffset) {
        TimeZone timeZone = TimeZoneUtil.getTimeZone(uctOffset);
        String dateStr = toString(new Date(date), Constant.DateTimeFormat.DATETIME_NO_SECONDS, timeZone);
        return toDate(dateStr, Constant.DateTimeFormat.DATETIME_NO_SECONDS);
    }
}
