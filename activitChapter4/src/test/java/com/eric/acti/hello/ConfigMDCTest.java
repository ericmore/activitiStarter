package com.eric.acti.hello;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class ConfigMDCTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mdc.cfg.xml");

    @Test
    @Deployment(resources = {"simpleErrorDelegate.bpmn20.xml"})
    public void test() {
//        LogMDC.setMDCEnabled(true);
        ProcessInstance myProcess = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        assertNotNull(myProcess);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        assertEquals("welcome to activiti", task.getName());

        activitiRule.getTaskService().complete(task.getId());
    }
}
