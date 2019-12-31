package coreapi;

import lombok.extern.slf4j.Slf4j;
import mapper.MyCustomerMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.SuspendedJobQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class ManagementServiceTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"simpleProcess-job.bpmn20.xml"})
    public void testJobQuery() {
        ManagementService managementService = activitiRule.getManagementService();
        List<Job> timerJobList = managementService.createTimerJobQuery().list();
        for (Job job : timerJobList) {
            log.info("timerJob = {}", job);
        }

        JobQuery jobQuery = managementService.createJobQuery();
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();

    }

    @Test
    @Deployment(resources = {"simpleProcess-job.bpmn20.xml"})
    public void testTablePageQuery() {
        ManagementService managementService = activitiRule.getManagementService();
        TablePage tablePage = managementService.createTablePageQuery()
                .tableName(managementService.getTableName(ProcessDefinitionEntity.class))
                .listPage(0, 100);

        List<Map<String, Object>> rows = tablePage.getRows();
        for (Map<String, Object> row : rows) {
            log.info("row = {}", row);
        }

    }


    @Test
    @Deployment(resources = {"simpleProcess-job.bpmn20.xml"})
    public void testCustomSQLQuery() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        ManagementService managementService = activitiRule.getManagementService();
        List<Map<String, Object>> mapList = managementService.executeCustomSql(new AbstractCustomSqlExecution<MyCustomerMapper, List<Map<String, Object>>>(MyCustomerMapper.class) {
            @Override
            public List<Map<String, Object>> execute(MyCustomerMapper o) {
                return o.findAll();
            }
        });

        for (Map<String, Object> map : mapList) {
            log.info("map = {}", map);
        }


    }

    @Test
    @Deployment(resources = {"simpleProcess-job.bpmn20.xml"})
    public void testCommand() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess");
        ManagementService managementService = activitiRule.getManagementService();
        ProcessDefinitionEntity myProcess = managementService.executeCommand(new Command<ProcessDefinitionEntity>() {
            @Override
            public ProcessDefinitionEntity execute(CommandContext commandContext) {
                ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessDefinitionEntityManager().findLatestProcessDefinitionByKey("myProcess");
                return processDefinitionEntity;
            }
        });

        log.info("ProcessDefinitionEntity = {}", myProcess);
    }
}
