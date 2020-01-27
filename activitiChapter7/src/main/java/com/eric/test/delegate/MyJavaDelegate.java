package com.eric.test.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import java.io.Serializable;

@Slf4j
public class MyJavaDelegate implements JavaDelegate, Serializable {

    private Expression name;
    private Expression desc;

    @Override
    public void execute(DelegateExecution execution) {
        if (name != null) {
            Object value = name.getValue(execution);
            log.info("name = {}", value);
        }
        if (desc != null) {
            Object value = desc.getValue(execution);
            log.info("desc = {}", value);
        }
        log.info("run my java delegate", this);
    }
}
