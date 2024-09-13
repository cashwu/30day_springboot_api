package com.demo.todolist.controller;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AppInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("customInfo", getCustomInfo());
    }

    private Map<String, Object> getCustomInfo() {
        Map<String, Object> customInfo = new HashMap<>();
        customInfo.put("version", "1.0.0");
        customInfo.put("description", "This is a custom info");
        customInfo.put("author", "cash");
        return customInfo;
    }
}