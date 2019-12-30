package coreapi;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

@Slf4j
public class TaskServiceTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"simpleProcess-task.bpmn20.xml"})
    public void testTaskService() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my document message!!!");
        runtimeService.startProcessInstanceByKey("myProcess", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        log.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        log.info("task.description = {}", task.getDescription());

        taskService.setVariable(task.getId(), "key1", "value1");
        taskService.setVariableLocal(task.getId(), "localKey1", "valueLocal1");

        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());
        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());

        log.info("taskServiceVariables = {}", taskServiceVariables);
        log.info("taskServiceVariablesLocal = {}", taskServiceVariablesLocal);
        log.info("processVariables = {}", processVariables);

        Map<String, Object> completVar = Maps.newHashMap();
        completVar.put("cKey1", "cValue1");
        taskService.complete(task.getId(), completVar);

        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        log.info("task1 = {}", task1);
    }
}
