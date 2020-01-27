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
public class ScriptTaskTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"simpleProcessScriptTask1.bpmn20.xml"})
    public void testScriptTask1() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstanceList) {
            log.info("variable = {}", historicVariableInstance);
        }
        log.info("variables.size = {}", historicVariableInstanceList.size());

    }

    @Test
    @Deployment(resources = {"simpleProcessScriptTask2.bpmn20.xml"})
    public void testScriptTask2() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 10);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstanceList) {
            log.info("variable = {}", historicVariableInstance);
        }
        log.info("variables.size = {}", historicVariableInstanceList.size());

    }

    @Test
    @Deployment(resources = {"simpleProcessScriptTask3.bpmn20.xml"})
    public void testScriptTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 10);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstanceList) {
            log.info("variable = {}", historicVariableInstance);
        }
        log.info("variables.size = {}", historicVariableInstanceList.size());

    }

    @Test
    public void testScriptEngine() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByName("juel");
        Object eval = engine.eval("${1 + 2}");
        log.info("value={}", eval);

    }

}
