package coreapi;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class RuntimeServiceTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testStartProcess() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        //start by key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);
        log.info("processInstance = {}", processInstance);

        //start by id
        ProcessInstance processInstance1 = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("processInstance1 = {}", processInstance1);
    }

    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testPRocessInstanceBuilder() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001")
                .processDefinitionKey("myProcess")
                .variables(variables)
                .start();
        log.info("processInstance = {}", processInstance);
    }


    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testVariables() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        //start by key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);
        log.info("processInstance = {}", processInstance);
        runtimeService.setVariable(processInstance.getId(), "key3", "value3");
        runtimeService.setVariable(processInstance.getId(), "key2", "newValue2");
        Map<String, Object> variablesResult = runtimeService.getVariables(processInstance.getId());
        log.info("variablesResult = {}", variablesResult);
    }


    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testProcessInstanceQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        //start by key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
        log.info("processInstance = {}", processInstance);

        ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        //processInstance1 is same with processInstance

        List<Execution> executionList = runtimeService.createExecutionQuery().listPage(0, 100);
        for (Execution execution : executionList) {
            log.info("execution = {}", execution);
        }

    }

    @Test
    @Deployment(resources = {"simple-process-trigger.bpmn20.xml"})
    public void testTrigger() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");

        Execution execution = runtimeService.createExecutionQuery().activityId("someTask").singleResult();
        log.info("execution before trigger={}", execution);
        runtimeService.trigger(execution.getId());
        execution = runtimeService.createExecutionQuery().activityId("someTask").singleResult();
        log.info("execution after trigger ={}", execution);
    }


    @Test
    @Deployment(resources = {"simple-process-signal-received.bpmn20.xml"})
    public void testSignalEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
        Execution execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("my-signal").singleResult();
        log.info("execution = {}", execution);

        runtimeService.signalEventReceived("my-signal");
        execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("my-signal").singleResult();
        log.info("execution = {}", execution);

    }


    @Test
    @Deployment(resources = {"simple-process-message-received.bpmn20.xml"})
    public void testMessageEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
        Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("my-message").singleResult();
        log.info("execution = {}", execution);

        runtimeService.messageEventReceived("my-message", execution.getId());
        execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("my-signal").singleResult();
        log.info("execution = {}", execution);

    }


    //基于message启动流程,本质是用过message找流程的key
    @Test
    @Deployment(resources = {"simple-process-message.bpmn20.xml"})
    public void testMessageStart() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService
//                .startProcessInstanceByKey("myProcess");
                .startProcessInstanceByMessage("my-message");
        log.info("processInstance = {}", processInstance);

    }

}
