package com.test.acti.hello;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

/**
 *
 */
@Slf4j
public class DemoMain {
    public static void main(String[] args) {
        log.info("start program");

        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String name = processEngine.getName();
        String version = processEngine.VERSION;

        log.info("name : {}  version: {}", name, version);


        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("my_approve.bpmn20.xml");
        Deployment deploy = deployment.deploy();
        String id = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(id).singleResult();
        log.info("ID: {} definition {},", id, processDefinition.getId());


        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        log.info("process start {}",processInstance.getProcessDefinitionKey());
        log.info("end program");
    }
}
