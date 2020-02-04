package com.eric.test.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import java.io.Serializable;
import java.util.Objects;

@Slf4j
public class MyPayDelegate implements JavaDelegate, Serializable {


    @Override
    public void execute(DelegateExecution execution) {
        Object errorflag = execution.getVariable("errorflag");
        if (Objects.equals(errorflag, true)) {
            throw new BpmnError("bpmnError");
        }
        log.info("MyPayDelegate");
    }
}
