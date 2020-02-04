package com.eric.test.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.io.Serializable;

@Slf4j
public class MyTakeDelegate implements JavaDelegate, Serializable {



    @Override
    public void execute(DelegateExecution execution) {

    }
}
