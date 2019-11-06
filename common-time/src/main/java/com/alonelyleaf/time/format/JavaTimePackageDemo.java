package com.alonelyleaf.time.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author bijl
 * @date 2019/11/5
 */
public class JavaTimePackageDemo {

    public static Long  systemCurrentTime(){

        return System.currentTimeMillis();
    }

    public static void date(){

        //LocalDateTime：只存储了日期和时间，如：2017-03-21T14:02:43.455。(后面的.455表示毫秒值的最后三位,使用.withNano(0)可把毫秒值设为0)
        LocalDateTime localDateTime = LocalDateTime.now();
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int dayOfMonth = localDateTime.getDayOfMonth();
        int dayOfYear = localDateTime.getDayOfYear();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();

        Date date = new Date();

    }

    //使用Date和SimpleDateFormat
    public String dateToString(){

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("G yyyy年MM月dd号 E a hh时mm分ss秒");

        String format = simpleDateFormat.format(new Date());

        System.out.println(format);
        //打印: 公元 2017年03月21号 星期二 下午 06时38分20秒

        return format;
    }

    //使用jdk1.8 LocalDateTime和DateTimeFormatter
    public String dateToString2(){

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter pattern =
                DateTimeFormatter.ofPattern("G yyyy年MM月dd号 E a hh时mm分ss秒");
        String format = now.format(pattern);
        System.out.println(format);
        //打印: 公元 2017年03月21号 星期二 下午 06时38分20秒
        return format;
    }

    //使用Date和SimpleDateFormat
    public Date stringToDate() throws ParseException {

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = simpleDateFormat.parse("2017-12-03 10:15:30");

        System.out.println(simpleDateFormat.format(date));
        //打印 2017-12-03 10:15:30
        return date;
    }

    //使用jdk1.8 LocalDateTime和DateTimeFormatter
    public LocalDateTime stringToDate2() throws ParseException {

        DateTimeFormatter pattern =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //严格按照ISO yyyy-MM-dd验证，03写成3都不行
        LocalDateTime dt = LocalDateTime.parse("2017-12-03 10:15:30",pattern);

        System.out.println(dt.format(pattern));
        return dt;
    }

    public void localDateTime(){

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.toString());
        System.out.println(now.getYear());
        System.out.println(now.getMonthValue());
        System.out.println(now.getDayOfMonth());
        System.out.println(now.getHour()); //24小时制
        System.out.println(now.getMinute());
        System.out.println(now.getSecond());
        System.out.println(now.getNano()); //毫秒值的后三位作为前三位后面补6个零

        /**
         * 打印的结果为:
         *         2017-03-21T20:26:18.317
         *         2017
         *         3
         *         21
         *         20
         *         26
         *         18
         *         317000000
         */

        //能够自定义时间
        LocalDateTime time1 = LocalDateTime.of(2017, 1, 1, 1, 1,1);
        System.out.println(time1); //2017-01-01T01:01:01

        //使用plus方法增加年份
        LocalDateTime time2 = LocalDateTime.of(2017, 1, 1, 1, 1,1);

        //改变时间后会返回一个新的实例nextYearTime
        LocalDateTime nextYearTime = time2.plusYears(1);

        System.out.println(nextYearTime); //2018-01-01T01:01:01

        //使用minus方法减年份
        LocalDateTime time3 = LocalDateTime.of(2017, 1, 1, 1, 1,1);
        LocalDateTime lastYearTime = time3.minusYears(1);
        System.out.println(lastYearTime); //2016-01-01T01:01:01

        //使用with方法设置月份
        LocalDateTime time4 = LocalDateTime.of(2017, 1, 1, 1, 1,1);
        LocalDateTime changeTime = time4.withMonth(12);
        System.out.println(changeTime); //2017-12-01T01:01:01

        //判断当前年份是否闰年
        //System.out.println("isLeapYear :" + time4.isLeapYear());

        //判断当前日期属于星期几
        LocalDateTime time5 = LocalDateTime.now();
        DayOfWeek dayOfWeek = time5.getDayOfWeek();
        System.out.println(dayOfWeek); //WEDNESDAY

    }

    public void instantDemo(){

        Instant now = Instant.now();
        System.out.println(now.toEpochMilli());//获取当前时间的毫秒值
        System.out.println(now.isAfter(now)); //当前时间是否在参数中的时间之后
        System.out.println(now.isBefore(now));//当前时间是否在参数中的时间之前

        //当前时间与参数中的时间进行对比,在参数的时间之前,相同,之后的值分别是(-1,0,1)
        System.out.println(now.compareTo(now));


        //用一个时间戳去创建一个Instance实例,用法和new Date(时间戳)创建一个Date实例是一样的
        Instant ofEpochMilli = Instant.ofEpochMilli(System.currentTimeMillis());

        System.out.println(now.getEpochSecond());//获取当前时间的秒
    }

    public void instantToLocalDateTime(){

        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());

        ZoneId systemDefault = ZoneId.systemDefault();

        LocalDateTime now = LocalDateTime.ofInstant(instant, systemDefault);

        System.out.println(now); //2017-03-22T13:44:34.979

    }

    public void LocalDateTimeToInstant(){

        LocalDateTime now = LocalDateTime.now();

        ZoneId systemDefault = ZoneId.systemDefault();

        Instant instant = now.atZone(systemDefault).toInstant();

        System.out.println(instant.toEpochMilli()); //1490163685578

    }

    public void durationDemo(){

        LocalDateTime start = LocalDateTime.of(2017, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2017, 2, 1, 1, 1);

        Duration result = Duration.between(start, end);
        System.out.println(result.toDays()); //31
        System.out.println(result.toHours()); //744
        System.out.println(result.toMinutes()); //44640
        System.out.println(result.toMillis()); //2678400000
        System.out.println(result.toNanos()); //2678400000000000
    }
}
