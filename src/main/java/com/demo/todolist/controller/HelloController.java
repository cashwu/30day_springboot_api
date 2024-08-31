package com.demo.todolist.controller;

import com.demo.todolist.config.TodoReminderProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final TodoReminderProperties reminderProperties;

    public HelloController(TodoReminderProperties reminderProperties) {
        this.reminderProperties = reminderProperties;
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
}
