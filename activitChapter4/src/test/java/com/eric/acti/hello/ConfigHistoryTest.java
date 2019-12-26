package com.eric.acti.hello;


import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class ConfigHistoryTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-history.cfg.xml");

    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void test() {
//        LogMDC.setMDCEnabled(true);


        //启动流程
        startProcess();

        //修改变量
        modifyVariable();

        //提交表单 task
        submitTaskFormData();


        //输出历史内容
        //输出历史活动
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            log.info("historicActivityInstance = {}", historicActivityInstance);
        }
        log.info("historicActivityInstances.size = {}", historicActivityInstances.size());

        //输出历史表单
        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().listPage(0, 100);

        for (HistoricTaskInstance historicInstance : historicTaskInstances) {
            log.info("historicTaskInstance = {}", historicInstance);
        }
        log.info("historicTaskInstances.Size: {}", historicActivityInstances.size());

        List<HistoricDetail> historicDetailsForm = activitiRule.getHistoryService()
                .createHistoricDetailQuery().formProperties().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetailsForm) {
            log.info("historicFormDetail = {}", toString(historicDetail));
        }
        log.info("historicFormDetail.size = {}", historicDetailsForm.size());

        //输出历史变量
        List<HistoricVariableInstance> historicVariableInstances = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            log.info("historicVariableInstance = {}", historicVariableInstance);
        }
        log.info("historicVariableInstances.size={}", historicVariableInstances.size());

        //输出历史详情
        List<HistoricDetail> historicDetails = activitiRule.getHistoryService().createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            log.info("historyDetail = {}", toString(historicDetail));
        }
        log.info("historyDetails.size = {}", historicDetails.size());
    }

    private void submitTaskFormData() {
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Map<String, String> properties = Maps.newHashMap();
        properties.put("formKey1", "value1");
        properties.put("formKey2", "value2");
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);
    }

    private void modifyVariable() {
        List<Execution> executions = activitiRule.getRuntimeService().createExecutionQuery().listPage(0, 100);
        for (Execution execution : executions) {
            log.info("execution = {}", execution);
        }
        log.info("execution.size = {}", executions.size());
        String id = executions.iterator().next().getId();
        activitiRule.getRuntimeService().setVariable(id, "keyStart1", "vvvvvvvvvvvv");
    }

    private void startProcess() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("keyStart1", "value1");
        params.put("keyStart2", "value2");
        ProcessInstance myProcess = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("myProcess", params);
    }

    static String toString(HistoricDetail historicDetail) {
        return ToStringBuilder.reflectionToString(historicDetail, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}