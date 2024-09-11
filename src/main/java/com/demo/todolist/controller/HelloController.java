package com.demo.todolist.controller;

import com.demo.todolist.config.TodoConfig;
import com.demo.todolist.config.TodoReminderProperties;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final TodoReminderProperties reminderProperties;

    @Autowired
    private TodoConfig todoConfig;

    public HelloController(TodoReminderProperties reminderProperties) {
        this.reminderProperties = reminderProperties;
    }

    @GetMapping("/")
    public String index() {
        return "Hello World";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/reminder-config")
    @Hidden
    public String getReminderConfig() {
        return "Threshold: " + reminderProperties.getThreshold() +
                ", Message: " + reminderProperties.getMessage();
    }

    @GetMapping("/randomNumber")
    @Hidden
    public String getRandomNumber() {
        return String.valueOf(todoConfig.getRandomNumber());
    }

    @GetMapping("/ex")
    @Hidden
    public String ex() {
        var a = 0;
        var b = 1;
        var c = b / a;
        return "";
    }
}
