package com.eric.activiti.bpmn20;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

@Slf4j
public class GatewayTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"exclusiveGateway1.bpmn20.xml"})
    public void testExclusiveGateway11() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("score", 70);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        log.info("task.name={}", task.getName());

    }


    @Test
    @Deployment(resources = {"parallelGateway1.bpmn20.xml"})
    public void testParallelGateway11() {
//        Map<String, Object> variables = Maps.newHashMap();
//        variables.put("score", 70);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");

        TaskService taskService = activitiRule.getTaskService();

        List<Task> list = activitiRule.getTaskService().createTaskQuery().list();
        for (Task task : list) {
            log.info("task.name={}", task.getName());
            taskService.complete(task.getId());
        }

        list = activitiRule.getTaskService().createTaskQuery().list();
        for (Task task : list) {
            log.info("task.name={}", task.getName());
        }


    }


}
