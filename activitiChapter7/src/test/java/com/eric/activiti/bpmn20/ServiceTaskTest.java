package com.eric.activiti.bpmn20;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class ServiceTaskTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"simpleProcessServiceTask1.bpmn20.xml"})
    public void testServiceTask1() {

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        List<HistoricVariableInstance> list = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            log.info("activiti = {}", historicVariableInstance);
        }
    }

    @Test
    @Deployment(resources = {"simpleProcessServiceTask3.bpmn20.xml"})
    public void testServiceTaskExpression() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("desc", "the test description");
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", map);

    }

    @Test
    @Deployment(resources = {"simpleProcessServiceTask2.bpmn20.xml"})
    public void testServiceTask2() {

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcessActBehavior");
        List<HistoricVariableInstance> list = activitiRule.getHistoryService().createHistoricVariableInstanceQuery()
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            log.info("activity = {}", historicVariableInstance);
        }

        Execution execution = activitiRule.getRuntimeService().createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        log.info("execution = {}", execution);

        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(new Command<Object>() {

            @Override
            public Object execute(CommandContext commandContext) {
                ActivitiEngineAgenda agenda = commandContext.getAgenda();
                agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) execution, false);
                return null;
            }
        });
        list = activitiRule.getHistoryService().createHistoricVariableInstanceQuery()
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            log.info("activity--- = {}", historicVariableInstance);
        }
    }


}
