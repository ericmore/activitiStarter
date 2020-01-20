package com.eric.test.listener;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.h2.util.StringUtils;
import org.joda.time.DateTime;

@Slf4j
public class MyTaskListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        log.info("config by listener");


        String eventName = delegateTask.getEventName();
        if (StringUtils.equals("create", eventName)) {
            delegateTask.addCandidateUsers(Lists.newArrayList("user1"));
            delegateTask.addCandidateGroup("group1");

            delegateTask.setVariable("key1", "value1");
            delegateTask.setDueDate(DateTime.now().plusDays(3).toDate());
        } else if (StringUtils.equals("complete", eventName)) {
            log.info("come to complete task");
        }

    }
}
