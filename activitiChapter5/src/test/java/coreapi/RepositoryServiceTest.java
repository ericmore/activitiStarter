package coreapi;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

@Slf4j
public class RepositoryServiceTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testRepository() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder1 = repositoryService.createDeployment();
        deploymentBuilder1.name("测试部署资源1")
                .addClasspathResource("simpleProcess.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder1.deploy();
        log.info("deploy = {}", deploy);

        DeploymentBuilder deploymentBuilder2 = repositoryService.createDeployment();
        deploymentBuilder2.name("测试部署资源2")
                .addClasspathResource("simpleProcess.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy1 = deploymentBuilder2.deploy();

        List<Deployment> deploymentList = repositoryService.createDeploymentQuery()
//                .deploymentId(deploy.getId())
                .orderByDeploymenTime().asc()
                .list();
        for (Deployment deployment : deploymentList) {
            log.info("deployment = {}", deployment);
        }
        log.info("deploymentList.size = {}", deploymentList.size());
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
//                .deploymentId(deploy.getId())
                .orderByProcessDefinitionKey().asc()
                .list();
        for (ProcessDefinition processDefinition : list) {
            log.info("流程定义文件 {}, 版本 {}, key {}, id {}",
                    processDefinition,
                    processDefinition.getVersion(),
                    processDefinition.getKey(),
                    processDefinition.getId());

        }
    }

    @Test

    @org.activiti.engine.test.Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        log.info("processDefinition.getId {}", processDefinition.getId());
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());

        RuntimeService runtimeService = activitiRule.getRuntimeService();

        try {
            log.info("启动开始...");
            runtimeService.startProcessInstanceById(processDefinition.getId());
            log.info("启动成功");
        } catch (Exception e) {
            log.error("启动失败 {}", e);
        }

        repositoryService.activateProcessDefinitionById(processDefinition.getId());
        try {
            log.info("启动开始第二次...");
            runtimeService.startProcessInstanceById(processDefinition.getId());
            log.info("启动成功第二次");
        } catch (Exception e) {
            log.error("启动失败第二次 {}", e);
        }
    }

    /**
     * 特定用户，用户组才能激活
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"simpleProcess.bpmn20.xml"})
    public void testCandidateStarter() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        log.info("processDefinition.getId {}", processDefinition.getId());

        repositoryService.addCandidateStarterUser(processDefinition.getId(), "user");
        repositoryService.addCandidateStarterGroup(processDefinition.getId(), "groupM");

        List<IdentityLink> identityLinksForProcessDefinition = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        for (IdentityLink identityLink : identityLinksForProcessDefinition) {
            log.info("identityLink = {}", identityLink);
        }
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(), "user");
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), "groupM");

    }
}
