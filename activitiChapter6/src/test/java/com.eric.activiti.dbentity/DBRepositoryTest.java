package com.eric.activiti.dbentity;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class DBRepositoryTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testDeploy() {
        activitiRule.getRepositoryService().createDeployment().name("二次审批流程")
//                .addClasspathResource("simpleProcess.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml")
                .deploy();
    }

    @Test
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.suspendProcessDefinitionById("second_approve:3:12504");

        log.info("second_approve:3:12504 是否被挂起 {}", repositoryService.isProcessDefinitionSuspended("second_approve:3:12504"));
    }

}
