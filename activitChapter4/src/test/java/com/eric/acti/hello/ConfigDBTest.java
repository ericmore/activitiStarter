package com.eric.acti.hello;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

@Slf4j
public class ConfigDBTest {

    @Test
    public void testConfig1() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        log.info("配置 {}", configuration);
        ProcessEngine processEngine = configuration.buildProcessEngine();

        log.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();

    }

    @Test
    public void testConfigDruid() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-druid.cfg.xml");
        log.info("配置 {}", configuration);
        ProcessEngine processEngine = configuration.buildProcessEngine();

        log.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();

    }
}
