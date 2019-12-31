package coreapi;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

@Slf4j
public class IdentityServiceTest1 {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testIdentity() {
        IdentityService identityService = activitiRule.getIdentityService();
        User user1 = identityService.newUser("user1");
        user1.setEmail("user1@ms.com");

        User user2 = identityService.newUser("user2");
        user2.setEmail("user2@ms.com");

        identityService.saveUser(user1);
        identityService.saveUser(user2);

        Group group1 = identityService.newGroup("group1");
        group1.setName("group name 1");
        identityService.saveGroup(group1);

        Group group2 = identityService.newGroup("group2");
        group2.setName("group name 2");
        identityService.saveGroup(group2);

        identityService.createMembership("user1", "group1");
        identityService.createMembership("user2", "group1");

        identityService.createMembership("user1", "group2");

        User user11 = identityService.createUserQuery().userId("user1").singleResult();
        user11.setLastName("Gu");
        identityService.saveUser(user11);
        
        List<User> userList = identityService.createUserQuery().memberOfGroup("group1").list();
        for (User user : userList) {
            log.info("user = {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }

        List<Group> groupList = identityService.createGroupQuery().groupMember("user1").list();
        for (Group group : groupList) {
            log.info("group = {}", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE));
        }


    }
}
