package com.eric.acti.hello.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class SimpleErrorDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        throw new RuntimeException("throw test");
    }
}
