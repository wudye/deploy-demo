package com.mwu.demo1.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BuildInfoController {

    private final BuildProperties buildProperties;

    public BuildInfoController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "mwu");
        map.put("age", "18");
        System.out.println("test method called");
        return map;
    }

    @GetMapping("/build-info")
    public Map<String, String> getBuildInfo() {
        Map<String, String> buildInfo = new HashMap<>();
        buildInfo.put("version", buildProperties.getVersion());
        buildInfo.put("time", buildProperties.getTime().toString());
        return buildInfo;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return status;
    }
}
