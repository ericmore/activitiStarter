package com.eric.acti.hello;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class ConfigInterceptTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-interceptor.cfg.xml");

    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void test() {
        ProcessInstance myProcess = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());

    }
}
