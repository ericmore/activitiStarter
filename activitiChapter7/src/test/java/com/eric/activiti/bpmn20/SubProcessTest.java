package com.eric.activiti.bpmn20;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class SubProcessTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"simpleProcessSubprocess.bpmn20.xml"})
    public void testSubprocess() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorflag", true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        log.info("task.name={}", task.getName());

    }



    @Test
    @Deployment(resources = {"simpleProcessEventSubprocess.bpmn20.xml"})
    public void testEventSubprocess(){
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorflag", true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", variables);
        List<Task> list = activitiRule.getTaskService().createTaskQuery().list();
        for (Task task : list) {
            log.info("task.name={}", task.getName());
        }


    }


}
