package com.alonelyleaf.time.dst.entity;

import java.io.Serializable;
import java.util.List;

public class WinTimeZone implements Serializable {

    /**
     * 主键
     */
    private String _id;

    /**
     * 时区ID
     */
    private String zoneId;

    /**
     * 时区偏移量
     */
    private Integer utcOffset;

    /**
     * 时区的UTC显示名
     */
    private String offsetDisplayName;

    /**
     * 时区的中文名
     */
    private String cnZoneName;

    /**
     * 时区的英文名
     */
    private String usZoneName;

    /**
     * 夏令时规则，无则为空
     */
    private List<Rule> rule;


    // ---------------------------- getter setter -------------------------------


    public String get_id() {
        return _id;
    }

    public WinTimeZone set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getZoneId() {
        return zoneId;
    }

    public WinTimeZone setZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public WinTimeZone setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
        return this;
    }

    public String getOffsetDisplayName() {
        return offsetDisplayName;
    }

    public WinTimeZone setOffsetDisplayName(String offsetDisplayName) {
        this.offsetDisplayName = offsetDisplayName;
        return this;
    }

    public String getCnZoneName() {
        return cnZoneName;
    }

    public WinTimeZone setCnZoneName(String cnZoneName) {
        this.cnZoneName = cnZoneName;
        return this;
    }

    public String getUsZoneName() {
        return usZoneName;
    }

    public WinTimeZone setUsZoneName(String usZoneName) {
        this.usZoneName = usZoneName;
        return this;
    }

    public List<Rule> getRule() {
        return rule;
    }

    public WinTimeZone setRule(List<Rule> rule) {
        this.rule = rule;
        return this;
    }
}
