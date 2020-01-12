package com.eric.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

public class DBHistoryTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testHistory() {
        activitiRule.getRepositoryService().createDeployment().name("测试部署")
                .addClasspathResource("simpleProcess.bpmn20.xml")
                .deploy();
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0", "value0");
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);

        runtimeService.setVariable(processInstance.getId(), "key1", "val1_1");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.setOwner(task.getId(), "eric");

        taskService.createAttachment("url",
                task.getId(), processInstance.getId(),
                "name", "desc", "/url/test.png");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note1");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note1");
        Map<String, String> properties = Maps.newHashMap();
        properties.put("key2","p1_val");
        properties.put("key3","p1_val");
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);

    }


}
