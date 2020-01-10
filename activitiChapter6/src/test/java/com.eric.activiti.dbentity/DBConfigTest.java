package com.eric.activiti.dbentity;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class DBConfigTest {

    @Test
    public void testDBConfig() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();
        Map<String, Long> tableCount = managementService.getTableCount();
        ArrayList<String> tableNames = Lists.newArrayList(tableCount.keySet());
        for (String tableName : tableNames) {
            log.info("table = {}", tableName);
        }
        log.info("tableNames.size= {}", tableNames.size());
    }

    /**
     * 删除整个schema表
     */
    @Test
    public void dropTable(){
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                commandContext.getDbSqlSession().dbSchemaDrop();
                log.info("删除表结构");
                return null;
            }
        });
    }
}
