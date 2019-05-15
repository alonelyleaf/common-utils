package com.alonelyleaf.guava.eventbus.event;

import com.alonelyleaf.guava.base.Event;

/**
 * @author bijl
 * @date 2019/5/15
 */
public class OfflineEvent implements Event {

    private String accountId;

    private Long offlineTime;

    public OfflineEvent(String accountId, Long offlineTime){
        this.accountId = accountId;
        this.offlineTime = offlineTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public OfflineEvent setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public Long getOfflineTime() {
        return offlineTime;
    }

    public OfflineEvent setOfflineTime(Long offlineTime) {
        this.offlineTime = offlineTime;
        return this;
    }
}
