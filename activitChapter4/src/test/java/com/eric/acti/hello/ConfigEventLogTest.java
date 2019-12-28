package com.eric.acti.hello;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class ConfigEventLogTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-eventlog.cfg.xml");

    @Test
    @Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void test() {
        ProcessInstance myProcess = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());

        List<EventLogEntry> eventLogEntries = activitiRule.getManagementService().getEventLogEntriesByProcessInstanceId(myProcess.getRootProcessInstanceId());
        for (EventLogEntry eventLogEntry : eventLogEntries) {
            log.info("eventlog type={} data= {}", eventLogEntry.getType(), new String(eventLogEntry.getData()));
        }
        log.info("eventLogEntries.size = {}", eventLogEntries);
    }
}
