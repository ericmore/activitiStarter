package com.eric.activiti.bpmn20;

import com.eric.test.delegate.MyJavaBean;
import com.eric.test.delegate.MyJavaDelegate;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti-spring.cfg.xml")
public class ServiceTaskSpringTest {

    @Resource
    @Rule
    public ActivitiRule activitiRule;

    @Test
    @Deployment(resources = {"simpleProcessServiceTask4.bpmn20.xml"})
    public void testServiceTask1() {

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        List<HistoricVariableInstance> list = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            log.info("activiti = {}", historicVariableInstance);
        }
    }

    @Test
    @Deployment(resources = {"simpleProcessServiceTask4.bpmn20.xml"})
    public void testServiceTask2() {
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        log.info("myJavaDelegate = {}", myJavaDelegate);
        variables.put("myJavaDelegate", myJavaDelegate);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", variables);
        List<HistoricVariableInstance> list = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            log.info("activiti = {}", historicVariableInstance);
        }
    }

    @Test
    @Deployment(resources = {"simpleProcessServiceTask5.bpmn20.xml"})
    public void testServiceTask5() {
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaBean myJavaBean = new MyJavaBean();
        myJavaBean.setName("name test");
        log.info("myJavaBean = {}", myJavaBean);
        variables.put("myJavaBean", myJavaBean);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess", variables);
        List<HistoricVariableInstance> list = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc().list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            log.info("activiti = {}", historicVariableInstance);
        }
    }


}
