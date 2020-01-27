package com.eric.test.delegate;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
public class MyJavaBean implements Serializable {

    private String name;

    public MyJavaBean() {
    }

    public MyJavaBean(String name) {
        this.name = name;
    }

    public String getName() {
        log.info("run getName name:{}", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sayHello() {
        log.info("run say hellosss");
    }
}
