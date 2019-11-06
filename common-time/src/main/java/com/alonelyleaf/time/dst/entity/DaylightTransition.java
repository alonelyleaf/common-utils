package com.alonelyleaf.time.dst.entity;


import java.io.Serializable;


public class DaylightTransition implements Serializable {

    /**
     * 是否使用固定模式，目前都是false表示浮动模式，true表示每年的固定几月几号
     */
    private String isFixedDateRule;

    /**
     * 第几天，只有isFixedDateRule=true才有用，目前没用
     */
    private String day;

    /**
     * 第几个月
     */
    private String month;

    /**
     * 第几周
     */
    private String week;

    /**
     * 一周中的周几，Sunday表示周日
     */
    private String dayOfWeek;

    /**
     * 规则的结束时间，0001-01-01 02:00:00，目前有用的是02:00:00
     */
    private String timeOfDay;


    // ---------------------------- getter setter -------------------------------

    public String getIsFixedDateRule() {
        return isFixedDateRule;
    }

    public DaylightTransition setIsFixedDateRule(String isFixedDateRule) {
        this.isFixedDateRule = isFixedDateRule;
        return this;
    }

    public String getDay() {
        return day;
    }

    public DaylightTransition setDay(String day) {
        this.day = day;
        return this;
    }

    public String getMonth() {
        return month;
    }

    public DaylightTransition setMonth(String month) {
        this.month = month;
        return this;
    }

    public String getWeek() {
        return week;
    }

    public DaylightTransition setWeek(String week) {
        this.week = week;
        return this;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public DaylightTransition setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public DaylightTransition setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
        return this;
    }

    @Override
    public String toString() {
        return "DaylightTransition{"
                + "isFixedDateRule='" + isFixedDateRule + '\''
                + ", day='" + day + '\''
                + ", month='" + month + '\''
                + ", week='" + week + '\''
                + ", dayOfWeek='" + dayOfWeek + '\''
                + ", timeOfDay='" + timeOfDay + '\''
                + '}';
    }
}
