package com.demo.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TodoConfig {
    
    private final int RandomNumber;

    public TodoConfig() {
        RandomNumber = (int) (Math.random() * 100);
    }

    public int getRandomNumber() {
        return RandomNumber;
    }
}
