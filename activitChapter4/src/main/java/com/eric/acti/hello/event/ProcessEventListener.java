package com.eric.acti.hello.event;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

@Slf4j
public class ProcessEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType type = activitiEvent.getType();
        if (ActivitiEventType.PROCESS_STARTED.equals(type)) {
            log.info("流程启动 {} \t {}", type, activitiEvent.getProcessInstanceId());
        } else if (ActivitiEventType.PROCESS_COMPLETED.equals(type)) {
            log.info("流程完成 {} \t {}", type, activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
