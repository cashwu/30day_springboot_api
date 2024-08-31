package com.demo.todolist.controller;

import com.demo.todolist.config.TodoConfig;
import com.demo.todolist.config.TodoReminderProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final TodoReminderProperties reminderProperties;
    private final TodoConfig todoConfig;

    public HelloController(TodoReminderProperties reminderProperties,
                           TodoConfig todoConfig) {
        this.reminderProperties = reminderProperties;
        this.todoConfig = todoConfig;
    }

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/reminder-config")
    public String getReminderConfig() {
        return "Threshold: " + reminderProperties.getThreshold() +
                ", Message: " + reminderProperties.getMessage();
    }

    @GetMapping("/randomNumber")
    public String getRandomNumber() {
        return String.valueOf(todoConfig.getRandomNumber());
    }
}
