package com.alonelyleaf.guava;

import com.alonelyleaf.guava.eventbus.event.OfflineEvent;
import com.alonelyleaf.guava.eventbus.publisher.EventPublisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonGuavaApplicationTests {

    @Autowired
    private EventPublisher eventPublisher;

    @Test
    public void pushOfflineEvent(){

        String accountId = "123";
        Long offlineTime = 1558527237L;

        OfflineEvent offlineEvent = new OfflineEvent(accountId, offlineTime);

        eventPublisher.publishOfflineEvent(offlineEvent);

        System.out.println("accountId = " + accountId + "offlineTime = " + offlineTime);
    }

}
