package com.eric.activiti.dbentity;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class DBIdentityTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testIdentity() {
        IdentityService identityService = activitiRule.getIdentityService();
        User user1 = identityService.newUser("user1");
        user1.setFirstName("Eric");
        user1.setLastName("Gu");
        user1.setEmail("eee@163.com");
        user1.setPassword("my password");
        identityService.saveUser(user1);

        Group group1 = identityService.newGroup("group1");
        group1.setName("test group1");
        identityService.saveGroup(group1);

        identityService.createMembership(user1.getId(), group1.getId());

        identityService.setUserInfo(user1.getId(), "age", "18");
        identityService.setUserInfo(user1.getId(), "address", "beijing");



    }


}
