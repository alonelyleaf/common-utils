package com.alonelyleaf.time.dst.util;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

    private DateUtil() {
    }

    /**
     * 给时间加上或减去指定毫秒，秒，分，时，天、月或年等，返回变动后的时间
     *
     * @param date   要加减前的时间，如果不传，则为当前日期
     * @param field  时间域，有Calendar.MILLISECOND,Calendar.SECOND,Calendar.MINUTE,<br>
     *               * Calendar.HOUR,Calendar.DATE, Calendar.MONTH,Calendar.YEAR
     * @param amount 按指定时间域加减的时间数量，正数为加，负数为减。
     * @return 变动后的时间
     */
    public static Date add(Date date,
                           int field,
                           int amount) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    public static Date add(Date date,
                           int field,
                           int amount,
                           TimeZone timeZone) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = Calendar.getInstance();
        if (timeZone != null) {
            cal.setTimeZone(timeZone);
        }
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    public static Date set(Date date,
                           int field,
                           int amount,
                           TimeZone timeZone) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = Calendar.getInstance();
        if (timeZone != null) {
            cal.setTimeZone(timeZone);
        }
        cal.setTime(date);
        cal.set(field, amount);
        return cal.getTime();
    }

    public static Date findDayOfYear(int dayOfWeekCode,
                                     int monthIndex,
                                     int week,
                                     TimeZone timeZone,
                                     Date currentYear) {
        Calendar currentCal = Calendar.getInstance(timeZone);
        currentCal.setFirstDayOfWeek(Calendar.MONDAY); // 测试发现，当前的数据库夏令时配置是从周一开始计算为一周的开始时间
        currentCal.setTime(currentYear);
        currentCal.setMinimalDaysInFirstWeek(1); //只有一天也当作一周
        currentCal.set(Calendar.MONTH, monthIndex - 1); //第几个月
        currentCal.set(Calendar.WEEK_OF_MONTH, week); //第几周
        currentCal.set(Calendar.DAY_OF_WEEK, dayOfWeekCode);
        return currentCal.getTime();
    }

    public static Date findDayOfMonth(int code,
                                      int index,
                                      Date date,
                                      TimeZone timeZone) {
        Calendar cal = createCalendarByTimeZone(date, timeZone);
        int firstDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (code > 0) { //
            return findWeekDay(index, firstDayOfMonth, lastDayOfMonth, cal, code);
        }

        if (code == -1) { // 周末
            return findWeekendDay(index, firstDayOfMonth, lastDayOfMonth, cal);
        }

        if (code == -2) {// 工作日
            return findWorkDay(index, firstDayOfMonth, lastDayOfMonth, cal);
        }

        if (code == -3) {// 日子，就是第一天
            cal.set(Calendar.DAY_OF_MONTH, index == -1 ? lastDayOfMonth : index);
            return cal.getTime();
        }

        return DateConvert.toDate("1970-01-01", "yyyy-MM-dd", timeZone);
    }

    /**
     * 星期日，星期一，星期二，星期三，星期四，星期五，星期六
     */
    public static Date findWeekDay(int index,
                                   int firstDayOfMonth,
                                   int lastDayOfMonth,
                                   Calendar cal,
                                   int code) {
        int count = 0;
        if (index == -1) { //最后星期几
            for (int i = lastDayOfMonth; i >= firstDayOfMonth; i--) {
                cal.set(Calendar.DAY_OF_MONTH, i);
                if (cal.get(Calendar.DAY_OF_WEEK) == code) {//找到最后一个星期几
                    return cal.getTime();
                }
            }
        } else {
            for (int i = firstDayOfMonth; i <= lastDayOfMonth; i++) {
                cal.set(Calendar.DAY_OF_MONTH, i);
                if (cal.get(Calendar.DAY_OF_WEEK) == code) {//找到具体的星期几
                    count++;
                }
                if (count == index) {
                    return cal.getTime();
                }
            }
        }
        return null;
    }

    public static Date findWeekendDay(int index,
                                      int firstDayOfMonth,
                                      int lastDayOfMonth,
                                      Calendar cal) {
        int count = 0;
        if (index == -1) { //最后周末
            for (int i = lastDayOfMonth; i >= firstDayOfMonth; i--) {
                cal.set(Calendar.DAY_OF_MONTH, i);
                if (!isWorkday(cal)) {
                    return cal.getTime();
                }
            }
        } else {
            for (int i = firstDayOfMonth; i <= lastDayOfMonth; i++) {
                cal.set(Calendar.DAY_OF_MONTH, i);
                if (cal.get(Calendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY) { //如果找到周六，就i++跳过周日
                    count++;
                } else if (cal.get(Calendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY) { //如果先找到的是周日，不跳过
                    count++;
                }
                if (count == index) {
                    return cal.getTime();
                }
            }
        }
        return null;
    }

    public static Date findWorkDay(int index,
                                   int firstDayOfMonth,
                                   int lastDayOfMonth,
                                   Calendar cal) {
        int count = 0;
        if (index == -1) { //最后工作日
            for (int i = lastDayOfMonth; i >= firstDayOfMonth; i--) { //从后往前算
                cal.set(Calendar.DAY_OF_MONTH, i);
                if (isWorkday(cal)) { //工作日
                    return cal.getTime();
                }
            }
        } else {
            for (int i = firstDayOfMonth; i <= lastDayOfMonth; i++) {
                cal.set(Calendar.DAY_OF_MONTH, i);
                if (isWorkday(cal)) { //工作日
                    count++;
                }
                if (count == index) {
                    return cal.getTime();
                }
            }
        }
        return null;
    }

    /**
     * @param calendar 日期
     * @return 是工作日返回true，非工作日返回false
     * @title 判断是否为工作日
     * @detail 工作日计算:
     * 1、正常工作日，并且为非假期
     * 2、周末被调整成工作日
     */
    public static boolean isWorkday(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
    }

    /**
     * 判断时间在给定的时区是不是工作日
     */
    public static boolean isWorkday(Date date,
                                    TimeZone timeZone) {
        return isWorkday(createCalendarByTimeZone(date, timeZone));
    }

    /**
     * 判断时间在给定的时区是不是特定的星期几，星期一，星期二...
     */
    public static boolean isDayOfWeek(Date date,
                                      TimeZone timeZone,
                                      Integer dayOfweekCode) {
        Calendar calendar = createCalendarByTimeZone(date, timeZone);
        return calendar.get(Calendar.DAY_OF_WEEK) == dayOfweekCode;
    }


    public static int findRealDayOfMonth(int dayOfMonth,
                                         Date date,
                                         TimeZone timeZone) {
        Calendar calendar = createCalendarByTimeZone(date, timeZone);

        int maxDayOfMonth = findMaxDayOfMonth(calendar);

        return returnRealDayOfMonth(dayOfMonth, maxDayOfMonth);
    }

    private static int returnRealDayOfMonth(int dayOfMonth,
                                            int maxDayOfMonth) {
        if (dayOfMonth > maxDayOfMonth) {
            return maxDayOfMonth;
        }
        return dayOfMonth;
    }

    private static Calendar createCalendarByTimeZone(Date date,
                                                     TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        if (timeZone != null) {
            calendar.setTimeZone(timeZone);
        }
        calendar.setTime(date);
        return calendar;
    }

    public static int findMaxDayOfMonth(Calendar cal) {
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 添加时区偏移量
     * @param offset 偏移量（以秒为单位）
     * @return 日期
     */
    public static String appendZoneSuffix(Integer offset) {

        if (offset == null) {
            return null;
        }
        //将偏移量转化为小时（小数去除不要）
        long hour = Long.valueOf((offset / 3600));
        //偏移量对小时取余数，得到小数（以秒为单位）
        double decimals = offset % 3600;
        //显示为09:30分钟形式
        double decimalsZone = (decimals / 3600) * 60 / 100;
        String sAdd = "";
        if (hour >= 0) {
            sAdd = "+";
        } else {
            sAdd = "-";
        }
        hour = hour > 0 ? hour : -hour;
        String sHour = hour + "";
        if (sHour.length() == 1) {
            sHour = '0' + sHour;
        }
        decimalsZone = decimalsZone < 0 ? -decimalsZone : decimalsZone;
        String sDecimalsZone = decimalsZone + "";
        sDecimalsZone = sDecimalsZone.substring(2);
        if (sDecimalsZone.length() == 1) {
            sDecimalsZone = sDecimalsZone + '0';
        } else if (sDecimalsZone.length() >= 3) {
            sDecimalsZone = sDecimalsZone.substring(0, 2);
        }
        return "GMT" + sAdd + sHour + ':' + sDecimalsZone;
    }


}
