package com.alonelyleaf.time.dst.entity;


import java.io.Serializable;


public class Rule implements Serializable {

    /**
     * 该夏令时规则生效的开始时间
     */
    private String dateStart;

    /**
     * 该夏令时规则生效的结束时间
     */
    private String dateEnd;

    /**
     * 夏令时偏移量默认01:00:00
     */
    private String daylightDelta;

    /**
     * 具体的夏令时开始时间规则
     */
    private DaylightTransition daylightTransitionStart;

    /**
     * 具体的夏令时结束时间规则
     */
    private DaylightTransition daylightTransitionEnd;


    // ---------------------------- getter setter -------------------------------

    public String getDateStart() {
        return dateStart;
    }

    public Rule setDateStart(String dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public Rule setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public String getDaylightDelta() {
        return daylightDelta;
    }

    public Rule setDaylightDelta(String daylightDelta) {
        this.daylightDelta = daylightDelta;
        return this;
    }

    public DaylightTransition getDaylightTransitionStart() {
        return daylightTransitionStart;
    }

    public Rule setDaylightTransitionStart(DaylightTransition daylightTransitionStart) {
        this.daylightTransitionStart = daylightTransitionStart;
        return this;
    }

    public DaylightTransition getDaylightTransitionEnd() {
        return daylightTransitionEnd;
    }

    public Rule setDaylightTransitionEnd(DaylightTransition daylightTransitionEnd) {
        this.daylightTransitionEnd = daylightTransitionEnd;
        return this;
    }
}
