package com.demo.todolist.controller;

import com.demo.todolist.model.Todo;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/form01")
    public String form01(String title) {
        return String.format("form01 - %s", title);
    }

    @PostMapping("/form02")
    public String form02(@RequestParam String title) {
        return String.format("form02 - %s", title);
    }

    @PostMapping("/form03")
    public String form03(Todo todo) {
        return String.format("form03 - %s", todo);
    }

    @PostMapping("/form04")
    public String form04(@RequestParam Map<String, Object> map) {
        String title = map.get("title").toString();
        return String.format("form04 - %s", title);
    }
}
