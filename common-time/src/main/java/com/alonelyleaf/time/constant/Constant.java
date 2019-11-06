package com.alonelyleaf.time.constant;

import com.alonelyleaf.util.StringPool;

/**
 * @author bijl
 * @date 2019/11/5
 */
public interface Constant {

    class Character implements StringPool {
        /**
         * 空格
         */
        public static final String WHITESPACE = SPACE;

    }

    class TrueFalse {

        public static final String FALSE = "false";

        public static final String TRUE = "true";
    }

    class DateTimeOperator {

        /**
         * 零时区
         */
        public static final String TIME_ZONE_UTC_ZERO = "GMT00:00";

        /**
         * 0点0时0分
         */
        public static final String ZERO_TIME = "00:00:00";

        /**
         * 一年
         */
        public static final int ONE_YEAR_LATER = 1;


    }

    class DateTimeMills {

        public static final long ONE_HOUR = 60 * 60 * 1000;

        public static final long ONE_DAY = 24 * 60 * 60 * 1000;

        public static final long HALF_DAY = 12 * 60 * 60 * 1000;

        public static final long ONE_WEEK = 7 * ONE_DAY;

        public static final long ONE_MONTH_MIN_MILLS = 28L * 24L * 60L * 60L * 1000L;

        public static final long ONE_YEAR_MIN_MILLS = 365L * 24L * 60L * 60L * 1000L;

        public static final long ONE_THOUSAND_YEAR_MIN_MILLS = 1000 * ONE_YEAR_MIN_MILLS;

        public static final long FIVE_MINUTE = 5 * 60 * 1000;

        public static final long ONE_MINUTE = 60 * 1000;

        public static final int ONE_YEARS_DAY = 365;

        public static final int ONE_YEAR_MONTH = 12;

        public static final int ONE_YEAR = 1;
    }

    class DateTimeFormat {

        public static final String DATE = "yyyy-MM-dd";

        public static final String DATE_NO_SEPARATOR = "yyyyMMdd";

        public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";

        public static final String HOUR_MINUTE = "HH:mm";

        public static final String DATETIME_WITH_SECONDS = "yyyy-MM-dd HH:mm:ss";

        public static final String DATETIME_WITH_SECONDS2 = "yyyy-MM-dd-HH-mm-ss";

        public static final String DATETIME_WITH_SECONDS3 = "yyyyMMddHHmmss";

        public static final String DATETIME_NO_SECONDS = "yyyy-MM-dd HH:mm";

        public static final String DATETIME_WITH_SECONDS_ISO = "yyyyMMdd'T'HHmmss";

        public static final String DATE_WITH_SLASH = "yyyy/MM/dd";
    }
}
