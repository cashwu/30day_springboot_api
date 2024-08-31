package com.demo.todolist.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${todo.reminder.threshold}")
    private int reminderThreshold;

    @Value("${todo.reminder.message}")
    private String reminderMessage;

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/reminder-config")
    public String getReminderConfig() {
        return "Threshold: " + reminderThreshold + ", Message: " + reminderMessage;
    }
}
