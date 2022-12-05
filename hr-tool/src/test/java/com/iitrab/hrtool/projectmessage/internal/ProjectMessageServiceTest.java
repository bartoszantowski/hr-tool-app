package com.iitrab.hrtool.projectmessage.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectMessageServiceTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

//    @Test
//    void shouldPublishEvent_whenPublishingEvent() {
//
//        //applicationEventPublisher.publishEvent(new ProjectMessageEvent());
//        //verify()
//    }

}
