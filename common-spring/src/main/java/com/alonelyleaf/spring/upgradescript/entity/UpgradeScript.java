package com.alonelyleaf.spring.upgradescript.entity;

public class UpgradeScript{

    private String _id;

    /**
     * 执行版本 对应脚本版本
     */
    private String version;

    public String get_id() {
        return _id;
    }

    public UpgradeScript set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public UpgradeScript setVersion(String version) {
        this.version = version;
        return this;
    }
}
