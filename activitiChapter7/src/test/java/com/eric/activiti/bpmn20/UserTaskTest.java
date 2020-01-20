package com.eric.activiti.bpmn20;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class UserTaskTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testUserTask() {
        ProcessInstance myProcess = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        log.info("find by user1 task = {}", task);
        task = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        log.info("find by user2 task = {}", task);
        task = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult();
        log.info("find by group1 task = {}", task);

        taskService.claim(task.getId(), "user2"); //this will do verification comparing to setAssignee, like completed task will throw exception when claim
//        taskService.setAssignee(task.getId(), "user2");
        log.info("claim task.id = {} by user2", task.getId());

        task = taskService.createTaskQuery().taskCandidateOrAssigned("user1").singleResult();
        log.info("find by user1 task={}", task); //expect null

        task = taskService.createTaskQuery().taskCandidateOrAssigned("user2").singleResult();
        log.info("find by user2 task={}", task); //expect null


    }

    @Test
    @Deployment(resources = {"simpleProcessListener.bpmn20.xml"})
    public void testUserTask2() {
        ProcessInstance myProcess = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        log.info("find by user1 task = {}", task);
        taskService.complete(task.getId());

    }
}
