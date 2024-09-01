package com.demo.todolist.controller;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/path01/{name}")
    public String path01(@PathVariable String name) {
        return String.format("path - %s", name);
    }

    @GetMapping("/path02/{name}")
    public String path02(@PathVariable(required = false) String name) {
        return String.format("path - %s", name != null ? name : "Unknown");
    }

    @GetMapping({"/path03/{name}", "/path03"})
    public String path03(@PathVariable(required = false) String name) {
        return String.format("path - %s", name != null ? name : "Unknown");
    }

    @GetMapping("/header")
    public String header(@RequestHeader(required = false) String name) {
        return String.format("header - %s", name);
    }

    @GetMapping("/cookie")
    public String cookie(@CookieValue(required = false) String name) {
        return String.format("cookie - %s", name);
    }
}
