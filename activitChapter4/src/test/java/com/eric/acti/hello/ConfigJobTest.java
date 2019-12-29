package com.eric.acti.hello;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

@Slf4j
public class ConfigJobTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-job.cfg.xml");

    @Test
    @Deployment(resources = {"simpleProcess-job.bpmn20.xml"})
    public void test() throws InterruptedException {
        log.info("start");
        List<Job> jobs = activitiRule.getManagementService().createTimerJobQuery().listPage(0, 100);
        for (Job job : jobs) {
            log.info("job = {}, exe-times = {}", job, job.getRetries());
        }
        log.info("job.size = {}", jobs.size());
        Thread.sleep(1000*100);
        log.info("end");


    }
}
