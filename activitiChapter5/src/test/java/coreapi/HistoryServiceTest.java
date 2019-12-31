package coreapi;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class HistoryServiceTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-history.cfg.xml");

    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testHistory() {
        HistoryService historyService = activitiRule.getHistoryService();
        ProcessInstanceBuilder processInstanceBuilder = activitiRule.getRuntimeService().createProcessInstanceBuilder();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0", "value0");
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        Map<String, Object> transientVariables = Maps.newHashMap();
        transientVariables.put("tKey1", "tValue1");
        ProcessInstance processInstance = processInstanceBuilder.processDefinitionKey("myProcess")
                .variables(variables)
                .transientVariables(transientVariables).start();

        activitiRule.getRuntimeService().setVariable(processInstance.getId(), "key1", "value_New1");

        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        Map<String, String> properties = Maps.newHashMap();
        properties.put("fKey1", "fValue1");
        properties.put("key2", "value_New2");
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);

        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().list();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            log.info("historicProcessInstance = {} ", ToStringBuilder.reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE));
        }

        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            log.info("historicActivityInstance = {}", historicActivityInstance);
        }

        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().list();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            log.info("historicTaskInstance = {}", ToStringBuilder.reflectionToString(historicTaskInstance, ToStringStyle.JSON_STYLE));
        }

        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().list();
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            log.info("historicVariableInstance = {}", historicVariableInstance);
        }

        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().list();
        for (HistoricDetail historicDetail : historicDetails) {
            log.info("historicDetail = {}", historicDetail);
        }

        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeVariables()
                .includeFormProperties()
                .includeComments()
                .includeActivities()
                .includeTasks()
                .includeVariableUpdates()
                .singleResult();

        List<HistoricData> historicData = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicDatum : historicData) {
            log.info("historicDatum = {}", historicDatum);
        }

        historyService.deleteHistoricProcessInstance(processInstance.getId());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        log.info("historicProcessInstance = {}", historicProcessInstance);
    }
}
