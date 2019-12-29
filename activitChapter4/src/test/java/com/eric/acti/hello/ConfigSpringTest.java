package com.eric.acti.hello;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.checkerframework.checker.units.qual.A;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ConfigSpringTest {

    @Rule
    @Autowired
    public ActivitiRule activitiRule;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    @Deployment(resources = {"simpleProcess-spring.bpmn20.xml"})
    public void test() {
        ProcessInstance myProcess = runtimeService.startProcessInstanceByKey("myProcess");
        assertNotNull(myProcess);

        Task task = taskService.createTaskQuery().singleResult();

        taskService.complete(task.getId());
    }
}
