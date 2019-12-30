package coreapi;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
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


    @Test
    @Deployment(resources = {"simpleProcess-task.bpmn20.xml"})
    public void testTaskServiceUser() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my document message!!!");
        runtimeService.startProcessInstanceByKey("myProcess", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        log.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        log.info("task.description = {}", task.getDescription());

        taskService.setOwner(task.getId(), "user1");
//        taskService.setAssignee(task.getId(), "jimmy");

        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("jimmy").taskUnassigned().list();
        for (Task task1 : taskList) {
            try {
                taskService.claim(task1.getId(), "jimmy");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            log.info("identityLink = {}", identityLink);
        }
        List<Task> jimmys = taskService.createTaskQuery().taskAssignee("jimmy").list();
        for (Task jimmy : jimmys) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("ckey1", "cval1");
            taskService.complete(jimmy.getId(), vars);
        }

        jimmys = taskService.createTaskQuery().taskAssignee("jimmy").list();
        log.info("Task jimmys = 是否存在 {}", !CollectionUtils.isEmpty(jimmys));
    }


    @Test
    @Deployment(resources = {"simpleProcess-task.bpmn20.xml"})
    public void testTaskServiceAttachment() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my document message!!!");
        runtimeService.startProcessInstanceByKey("myProcess", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        taskService.createAttachment("url",
                task.getId(),
                task.getProcessInstanceId(),
                "name",
                "attachement descrption",
                "/url/test.png");

        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            log.info("taskAttachment = {}", ToStringBuilder.reflectionToString(taskAttachment, ToStringStyle.JSON_STYLE));
        }
    }

    @Test
    @Deployment(resources = {"simpleProcess-task.bpmn20.xml"})
    public void testTaskServiceComment() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my document message!!!");
        runtimeService.startProcessInstanceByKey("myProcess", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        taskService.setOwner(task.getId(), "user1");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "my comments!!!!");

        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            log.info("taskComment = {}", ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE));
        }

        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents) {
            log.info("taskEvent = {}", ToStringBuilder.reflectionToString(taskEvent, ToStringStyle.JSON_STYLE));
        }
    }
}
