package com.alonelyleaf.util;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public interface ConstantPool {

    /**
     * 常用日期格式
     */
    interface DateFormat {

        /**
         * 日期格式
         */
        String NORMAL_DATE_FORMAT = "yyyy-MM-dd";

        /**
         * 日期 + 小时 + 分钟格式
         */
        String DATE_HOUR_MIN_FORMAT = "yyyy-MM-dd HH:mm";

        /**
         * 日期 + 小时 + 分钟 + 秒格式
         */
        String DATE_HOUR_MIN_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";

        /**
         * 全部的时间格式
         */
        String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    }

    /**
     * 常用时间常量
     */
    interface Timestamp {

        long ONE_DAY = TimeUnit.DAYS.toMillis(1);

        long ONE_YEAR = 365L * ONE_DAY;

        long MIN_ONE_MONTH = 28L * ONE_DAY;
    }

    /**
     * 常用长度
     */
    interface Length {

        int LENGTH_1 = 1;

        int LENGTH_2 = 2;

        int LENGTH_3 = 3;

        int LENGTH_4 = 4;

        int LENGTH_8 = 8;

        int LENGTH_10 = 10;

        int LENGTH_16 = 16;

        int LENGTH_20 = 20;

        int LENGTH_30 = 30;

        int LENGTH_32 = 32;

        int LENGTH_64 = 64;

        int LENGTH_100 = 100;

        int LENGTH_128 = 128;

        int LENGTH_300 = 300;

        int LENGTH_512 = 512;

        int LENGTH_1000 = 1000;
    }

    /**
     * 常用正则表达式
     */
    interface Reg {

        /**
         * int类型正则表达式
         */
        String INTEGER = ".*\\d+.*";
        Pattern INTEGER_PATTERN = Pattern.compile(INTEGER);

        /**
         * 以13 14 15 17 18 19 开头的11位号码
         */
        String MOBILE_REG = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern MOBILE_PATTERN = Pattern.compile(MOBILE_REG);

        /**
         * email正则表达式
         */
        String EMAIL_REG = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REG);

        String PASSWORD_REG = "^(?![a-zA-Z]+$)(?!\\d+$)(?![@*_\\-.]+$)[a-zA-Z\\d@*_\\-.]{8,64}$";
        Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REG);

        /**
         * 匹配点号
         */
        String DOT_REG = "\\.";
        Pattern DOT_PATTERN = Pattern.compile(DOT_REG);

        /**
         * 字母正则表达式
         */
        String ALPHABET = "^[a-zA-Z]+$";
        Pattern ALPHABET_PATTERN = Pattern.compile(ALPHABET);

        /**
         * 数值字符串，匹配0开头的数值
         */
        String DIGITAL = "^[0-9]+$";
        Pattern DIGITAL_PATTERN = Pattern.compile(DIGITAL);

        /**
         * 常用英文字符，包括大小写字母、数字和下划线
         */
        String NORMAL_WILDCARD = "^\\w+$";
        Pattern NORMAL_WILDCARD_PATTERN = Pattern.compile(NORMAL_WILDCARD);
    }
}
