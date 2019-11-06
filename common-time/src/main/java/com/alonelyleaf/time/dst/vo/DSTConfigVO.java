package com.alonelyleaf.time.dst.vo;

/**
 *         夏令时模式
 *         是否开启夏令时
 *         夏令时偏移量
 */
public class DSTConfigVO {


    /**
     * 夏令时偏移量
     */
    private long dayLightDelta;

    /**
     * 系统是否默认开启夏令时
     */
    private int defaultDSTEnable;

    /**
     * 企业默认时区
     */
    private String defaultTimeZone;


    public DSTConfigVO() {
    }

    public DSTConfigVO(long dayLightDelta, int defaultDSTEnable) {
        this.dayLightDelta = dayLightDelta;
        this.defaultDSTEnable = defaultDSTEnable;
    }

    public DSTConfigVO(long dayLightDelta, int defaultDSTEnable, String defaultTimeZone) {
        this.dayLightDelta = dayLightDelta;
        this.defaultDSTEnable = defaultDSTEnable;
        this.defaultTimeZone = defaultTimeZone;
    }

    public long getDayLightDelta() {
        return dayLightDelta;
    }

    public void setDayLightDelta(final long dayLightDelta) {
        this.dayLightDelta = dayLightDelta;
    }

    public int getDefaultDSTEnable() {
        return defaultDSTEnable;
    }

    public DSTConfigVO setDefaultDSTEnable(final int defaultDSTEnable) {
        this.defaultDSTEnable = defaultDSTEnable;
        return this;
    }

    public String getDefaultTimeZone() {
        return defaultTimeZone;
    }

    public DSTConfigVO setDefaultTimeZone(String defaultTimeZone) {
        this.defaultTimeZone = defaultTimeZone;
        return this;
    }
}
