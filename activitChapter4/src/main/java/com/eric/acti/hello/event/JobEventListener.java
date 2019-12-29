package com.eric.acti.hello.event;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

@Slf4j
public class JobEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType type = activitiEvent.getType();
        String name = type.name();

        if (name.startsWith("TIMEER") || name.startsWith("JOB")) {
            log.info("监听Job事件 {} ", type, activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
