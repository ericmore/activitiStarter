package com.eric.acti.hello;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

@Slf4j
public class ConfigTest {

    @Test
    public void testConfig1() {
        ProcessEngineConfiguration processEngineConfigurationFromResourceDefault = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        log.info("configuration = {}", processEngineConfigurationFromResourceDefault);
    }

    @Test
    public void testConfig2(){
        ProcessEngineConfiguration standaloneProcessEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        log.info("configuration = {}", standaloneProcessEngineConfiguration);
    }
}
