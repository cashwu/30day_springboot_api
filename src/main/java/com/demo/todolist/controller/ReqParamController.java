package com.demo.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReqParamController {

    @GetMapping("/query01")
    public String query01(String name) {
        return String.format("query string - %s", name);
    }

    @GetMapping("/query02")
    public String query02(@RequestParam String name) {
        return String.format("query string - %s", name);
    }

    @GetMapping("/query03")
    public String query03(@RequestParam(required = false, defaultValue = "Guest") String name) {
        return String.format("query string - %s", name);
    }
}
