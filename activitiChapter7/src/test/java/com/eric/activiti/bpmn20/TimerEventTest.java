package com.eric.activiti.bpmn20;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

@Slf4j
public class TimerEventTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-timer-boundary.bpmn20.xml"})
    public void testTimerBoundary() throws InterruptedException {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        List<Task> list = activitiRule.getTaskService().createTaskQuery().list();
        for (Task task : list) {
            log.info("task.name ={}", task.getName());
        }
        log.info("task.size = {}", list.size());

        Thread.sleep(1000 * 15);

        list = activitiRule.getTaskService().createTaskQuery().list();
        for (Task task : list) {
            log.info("task.name ={}", task.getName());
        }
        log.info("task.size = {}", list.size());


    }
}
