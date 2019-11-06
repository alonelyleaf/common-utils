package com.alonelyleaf.time.dst.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: CHNan
 * @date 2017/7/7
 */
public enum DayOfWeek {

    DAY("Day", -3, "日子"),

    WEEKDAY("Weekday", -2, "工作日"),

    WEEKENDDAY("WeekendDay", -1, "周末"),

    SUNDAY("Sunday", 1, "星期日"),

    MONDAY("Monday", 2, "星期一"),

    TUESDAY("Tuesday", 3, "星期二"),

    WEDNESDAY("Wednesday", 4, "星期三"),

    THURSDAY("Thursday", 5, "星期四"),

    FRIDAY("Friday", 6, "星期五"),

    SATURDAY("Saturday", 7, "星期六");


    private String label;
    private int code;
    private String zhLocalName;

    DayOfWeek(String label, int code, String zhLocalName) {
        this.label = label;
        this.code = code;
        this.zhLocalName = zhLocalName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getZhLocalName() {
        return zhLocalName;
    }

    public void setZhLocalName(String zhLocalName) {
        this.zhLocalName = zhLocalName;
    }

    public static List<String> getLabels() {
        DayOfWeek[] dayOfWeeks = DayOfWeek.values();
        List<String> labels = new ArrayList<>();
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            labels.add(dayOfWeek.getLabel());
        }
        return labels;
    }

    public static DayOfWeek getByLabel(String label) {
        DayOfWeek[] dayOfWeeks = DayOfWeek.values();
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            if (dayOfWeek.getLabel().equalsIgnoreCase(label)) {
                return dayOfWeek;
            }
        }
        return SUNDAY;
    }

    public static List<DayOfWeek> getWorkDaysByLabels(List<String> labels) {
        DayOfWeek[] dayOfWeeks = DayOfWeek.values();
        List<DayOfWeek> workDays = new ArrayList<>();
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            if (labels.contains(dayOfWeek.getLabel())) {
                workDays.add(dayOfWeek);
            }
        }
        return workDays;
    }

}
