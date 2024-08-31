package com.demo.todolist.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TodoConfig implements InitializingBean, DisposableBean {
    
    private final int RandomNumber;

    public TodoConfig() {
        RandomNumber = (int) (Math.random() * 100);
    }

    public int getRandomNumber() {
        return RandomNumber;
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean is going through init.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Bean's properties are set.");
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Bean will destroy now.");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Bean is destroying.");
    }
}
