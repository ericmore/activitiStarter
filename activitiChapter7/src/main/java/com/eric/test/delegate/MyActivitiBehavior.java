package com.eric.test.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;

@Slf4j
public class MyActivitiBehavior implements ActivityBehavior {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("run my activity behavior");
    }
}
