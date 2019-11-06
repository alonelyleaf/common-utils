package com.alonelyleaf.time.dst.vo;

import java.util.Date;

public class DSTRule {

    private Date dateStart;

    private Date dateEnd;

    private Long dayLightDelta;

    public DSTRule(Date dateStart, Date dateEnd, Long dayLightDelta) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dayLightDelta = dayLightDelta;
    }

    public DSTRule() {
    }

    public Date getDateStart() {
        return dateStart;
    }

    public DSTRule setDateStart(Date dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public DSTRule setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public Long getDayLightDelta() {
        return dayLightDelta;
    }

    public DSTRule setDayLightDelta(Long dayLightDelta) {
        this.dayLightDelta = dayLightDelta;
        return this;
    }

    @Override
    public String toString() {
        return "DaylightTransitionDate{"
                + "dateStart="
                + dateStart
                + ", dateEnd="
                + dateEnd
                + ", dayLightDelta="
                + dayLightDelta
                + '}';
    }
}