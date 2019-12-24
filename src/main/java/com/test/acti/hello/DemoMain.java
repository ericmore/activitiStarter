package com.test.acti.hello;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.util.AutoPopulatingList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
@Slf4j
public class DemoMain {
    public static void main(String[] args) throws ParseException {
        log.info("start program");

        ProcessEngine processEngine = getProcessEngine();


        ProcessDefinition processDefinition = getProcessDefinition(processEngine);


        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

        executeTask(processEngine, processInstance);


//        log.info("process start {}", processInstance.getProcessDefinitionKey());
        log.info("end program");
    }

    private static void executeTask(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        while (processInstance != null && !processInstance.isEnded()) {
            TaskService taskService = processEngine.getTaskService();
            List<Task> taskList = taskService.createTaskQuery().list();
            for (Task task : taskList) {
                log.info("task list:{}", task.getName());
                FormService formService = processEngine.getFormService();
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormProperty> formProperties = taskFormData.getFormProperties();
                HashMap<String, Object> variables = Maps.newHashMap();
                for (FormProperty property : formProperties
                ) {
                    log.info("please input {} ?", property.getName());
                    String line = scanner.nextLine();
                    if (StringFormType.class.isInstance(property.getType())) {
                        variables.put(property.getId(), line);
                    } else if (DateFormType.class.isInstance(property.getType())) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = simpleDateFormat.parse(line);
                        variables.put(property.getId(), date);
                    } else {
                        log.info("not supported date type");
                    }


                }
                variables.forEach((k, v) -> log.info(" {} = {} ", k, v));
                taskService.complete(task.getId(), variables);
                RuntimeService runtimeService = processEngine.getRuntimeService();
                processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();


            }
            log.info("task size: {}", taskList.size());
        }
        scanner.close();
    }

    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        return runtimeService.startProcessInstanceById(processDefinition.getId());
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("my_approve.bpmn20.xml");
        Deployment deploy = deployment.deploy();
        String id = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(id).singleResult();
        log.info("ID: {} definition {},", id, processDefinition.getId());
        return processDefinition;
    }

    private static ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String version = processEngine.VERSION;
        String name = processEngine.getName();
        log.info("name : {}  version: {}", name, version);
        return processEngine;
    }
}
