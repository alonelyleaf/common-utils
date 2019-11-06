package com.alonelyleaf.time.dst.util;

import com.alonelyleaf.time.constant.Constant;
import com.alonelyleaf.time.dst.entity.DaylightTransition;
import com.alonelyleaf.time.dst.entity.Rule;
import com.alonelyleaf.time.dst.entity.WinTimeZone;
import com.alonelyleaf.time.dst.vo.DSTRule;
import com.alonelyleaf.util.ValidateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.alonelyleaf.time.constant.Constant.DateTimeMills.ONE_YEAR_MIN_MILLS;


/**
 * @author ChNan
 */
public class DSTUtil {

    /**
     * 获取夏令时规则
     *
     * @param winTimeZone     当前时区
     * @param dstEnable       当前是否开启夏令时
     * @param checkedDateTime 当前开始时间，从什么时候开始计算
     */
    public static DSTRule getDSTRule(WinTimeZone winTimeZone, Integer dstEnable, Long checkedDateTime) {
        if (dstInvalid(dstEnable)) {
            return null;
        }

        return getDSTRule(winTimeZone, checkedDateTime);
    }

    /**
     * @param winTimeZone     当前时区
     * @param checkedDateTime 表示从什么时候开始判断是否夏令时
     */
    public static DSTRule getDSTRule(WinTimeZone winTimeZone, Long checkedDateTime) {
        Rule rule = getWinTimeZoneRule(winTimeZone, checkedDateTime);
        if (rule == null) {
            return null;
        }

        Date dstStartDate = getDSTDate(rule.getDaylightTransitionStart(), new Date(checkedDateTime));
        Date dstEndDate = getDSTEndDate(checkedDateTime, rule.getDaylightTransitionEnd(), dstStartDate);

        // 如果今年没找到，往前推一年。因为考虑这样的例子。夏令时规则是2018-10-1 到2019-02-01，然后今天是2018-01-04号。
        // 实际上它是落在 2017-10-1 到2018-02-01 这个规则里面
        if (existDSTRule(checkedDateTime, dstStartDate, dstEndDate)) {
            return constructDSTRule(rule, dstStartDate, dstEndDate);
        }else{
            Long checkedDateOneYearAgo = checkedDateTime - ONE_YEAR_MIN_MILLS;
            rule = getWinTimeZoneRule(winTimeZone, checkedDateOneYearAgo);
            if (rule == null) {
                return null;
            }

            dstStartDate = getDSTDate(rule.getDaylightTransitionStart(), new Date(checkedDateOneYearAgo));
            dstEndDate = getDSTEndDate(checkedDateOneYearAgo, rule.getDaylightTransitionEnd(), dstStartDate);
            if (existDSTRule(checkedDateTime, dstStartDate, dstEndDate)) {
                return constructDSTRule(rule, dstStartDate, dstEndDate);
            }
        }

        return null;
    }

    /**
     * 获取夏令时规则
     *
     * @param winTimeZone     当前时区
     * @param checkedDateTime 当前开始时间，从什么时候开始计算,yyyy-MM-dd hh:mm:ss格式
     */
    public static DSTRule getDSTRule(WinTimeZone winTimeZone, String checkedDateTime) {

        return getDSTRule(winTimeZone, DateConvert.toDateTimeWithSecond(checkedDateTime, winTimeZone.getUtcOffset()).getTime());
    }

    /**
     * 根据传进来的时区和时间，生成该时间的有在夏令时情况下的时间戳
     *
     * @param winTimeZone
     * @param needTransformTime yyyy-MM-dd HH:mm:ss
     */
    public static Long transform2DSTTime(WinTimeZone winTimeZone, String needTransformTime) {

        Long needTransformTimeStamp = DateConvert.toDateTimeWithSecond(needTransformTime, winTimeZone.getUtcOffset()).getTime();

        DSTRule dstRule = getDSTRule(winTimeZone, needTransformTimeStamp);

        if (checkHasDST(needTransformTimeStamp, dstRule)) {
            return needTransformTimeStamp - dstRule.getDayLightDelta();
        }
        return needTransformTimeStamp;
    }


    /**
     * 根据传进来的时区和时间，生成该时间的有在夏令时情况下的时间格式化
     * 例如北京时区:2017-09-01 12:00:00
     * 乌兰巴托时区:2017-09-01 13:00:00
     *
     * @param checkTime
     * @param winTimeZone 时区
     * @param format      yyyy-MM-dd HH:mm:ss
     */
    public static String transform2TimeFormat(Long checkTime, WinTimeZone winTimeZone, String format) {
        DSTRule dstRule = DSTUtil.getDSTRule(winTimeZone, checkTime);

        Long dayLightDelta = 0L;
        if (dstRule != null) {
            dayLightDelta = dstRule.getDayLightDelta();
        }

        checkTime = checkTime + dayLightDelta;
        return DateConvert.toString(new Date(checkTime), format,
                TimeZoneUtil.getTimeZone(winTimeZone.getUtcOffset())
        );
    }

    public static boolean checkHasDST(Long startDateTime, DSTRule dstRule) {
        return dstRule != null && dstRule.getDateStart().getTime() <= startDateTime && startDateTime <= dstRule.getDateEnd().getTime();
    }

    //-------------------------------------------------------------private ---------------------------------------------------------

    /**
     * 获取该夏令时结束时间
     */
    private static Date getDSTEndDate(Long checkedDateTime, DaylightTransition daylightTransitionEnd, Date dstStartDate) {
        Date dstEndDate = getDSTDate(daylightTransitionEnd, new Date(checkedDateTime));

        if (endDateNotExistThisYear(dstStartDate, dstEndDate)) { //今年的结束时间算出来比开始时间小，说明结束时间是落在明年的区间里面。
            dstEndDate = getDSTDate(daylightTransitionEnd,
                    DateUtil.add(new Date(checkedDateTime), Calendar.YEAR, Constant.DateTimeOperator.ONE_YEAR_LATER)
            );
        }

        return dstEndDate;
    }

    /**
     * 构造夏令时返回规则
     */
    private static DSTRule constructDSTRule(Rule rule, Date dateStart, Date dateEnd) {
        //两个相减算出偏移量
        String currentDate = DateConvert.toString(new Date(), Constant.DateTimeFormat.DATE);

        return new DSTRule(dateStart, dateEnd,
                minusToGetDelta(
                        getDeltaTime(rule, currentDate),
                        getZeroTime(currentDate)
                )
        );
    }

    //2017-01-01 01:00:00
    private static Date getDeltaTime(Rule rule, String currentDate) {
        return DateConvert.toDateTimeWithSecond(
                createFullDateTimeStr(currentDate, rule.getDaylightDelta()),
                TimeZoneUtil.getZeroTimeZone()
        );
    }

    private static Date getZeroTime(String currentDate) {
        return DateConvert.toDateTimeWithSecond(
                createFullDateTimeStr(currentDate, Constant.DateTimeOperator.ZERO_TIME),
                TimeZoneUtil.getZeroTimeZone()
        );
    }

    /**
     * 获取夏令时偏移量的毫秒数
     */
    private static long minusToGetDelta(Date dayLightDeltaToDate, Date zeroDate) {
        return dayLightDeltaToDate.getTime() - zeroDate.getTime();
    }

    /**
     * @param dstEnable 夏令时无效，或者没有开始强夏令时
     */
    private static boolean dstInvalid(Integer dstEnable) {
        return dstEnable == null || dstEnable != 1;
    }

    private static boolean endDateNotExistThisYear(Date dateStart, Date dateEnd) {
        return dateEnd != null && dateStart != null && dateEnd.before(dateStart);
    }

    /**
     * @param currentDate 2017-07-07
     * @param time        02:00:00
     */
    private static String createFullDateTimeStr(String currentDate, String time) {
        return currentDate + Constant.Character.WHITESPACE + time;
    }

    /**
     * @param daylightTransition 当前时区的夏令时开始规则或者结束规则
     * @param currentDate        当前时区的下令开始时间或者结束时间
     */
    private static Date getDSTDate(DaylightTransition daylightTransition, Date currentDate) {
        if (daylightTransition.getIsFixedDateRule().equalsIgnoreCase(Constant.TrueFalse.FALSE)) {

            // 2017年5月的第4个星期六，获取那一天详细的日期信息
            Date dateOfMonth = findDayOfMonth(daylightTransition, currentDate);

            // 构造出具体的夏令时开始的那一天的日期和时间，例如 2017-5-27 02:00:00
            String dateTimeStr = createFullDateTimeStr(
                    DateConvert.toString(dateOfMonth, Constant.DateTimeFormat.DATE),
                    daylightTransition.getTimeOfDay().split(Constant.Character.WHITESPACE)[1]
            );

            return DateConvert.toDateTimeWithSecond(dateTimeStr, TimeZoneUtil.getZeroTimeZone());
        }
        return null;
    }

    private static Date findDayOfMonth(DaylightTransition daylightTransition, Date currentDate) {
        return DateUtil.findDayOfYear(DayOfWeek.getByLabel(daylightTransition.getDayOfWeek()).getCode(),
                Integer.valueOf(daylightTransition.getMonth()),
                Integer.valueOf(daylightTransition.getWeek()), TimeZone.getDefault(), currentDate);
    }

    /**
     * 判断当前时间是否是在规则的范围内，并且获取最简单的规则
     */
    private static Rule getWinTimeZoneRule(WinTimeZone winTimeZone, long checkDateTime) {
        List<Rule> rules = winTimeZone.getRule();
        if (ValidateUtil.isEmpty(rules)) {
            return null;
        }

        for (Rule rule : rules) {
            Date dateStart = DateConvert.toDateTimeWithSecond(rule.getDateStart());
            Date dateEnd = DateConvert.toDateTimeWithSecond(rule.getDateEnd());
            if (existValidRule(checkDateTime, dateStart, dateEnd)) {
                return rule;
            }
        }

        return null;
    }

    private static boolean existDSTRule(Long checkedDateTime, Date dateStart, Date dateEnd) {
        return dateStart != null && dateEnd != null && existValidRule(checkedDateTime, dateStart, dateEnd);
    }

    /**
     * 判断当前时间是否是在规则的范围内，通过夏令时开始时间和结束时间判断
     */
    private static boolean existValidRule(long checkDateTime, Date dateStart, Date dateEnd) {
        return dateStart.getTime() <= checkDateTime && checkDateTime <= dateEnd.getTime();
    }

}
