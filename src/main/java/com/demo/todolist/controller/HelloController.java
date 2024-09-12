package com.demo.todolist.controller;

import com.demo.todolist.config.TodoConfig;
import com.demo.todolist.config.TodoReminderProperties;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/authInfo")
    public String authInfo(Authentication auth) {

        return "Authentication type: " + auth.getClass().getSimpleName()
                + "\nName: " + auth.getName()
                + "\nAuthorities: " + auth.getAuthorities()
                + "\nDetails: " + auth.getDetails()
                + "\nCredentials: " + auth.getCredentials()
                + "\nPrincipal: " + auth.getPrincipal();
    }

    @GetMapping("/reminder-config")
    @Hidden
    public String getReminderConfig() {
        return "Threshold: " + reminderProperties.getThreshold() + ", Message: "
                + reminderProperties.getMessage();
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
