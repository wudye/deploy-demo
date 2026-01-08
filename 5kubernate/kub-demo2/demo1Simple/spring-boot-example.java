package com.mwu.demo1simple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigDemoController {

    // 这些值将从 ConfigMap 挂载的文件中读取
    @Value("${app.message:Default Message}")
    private String appMessage;
    
    @Value("${app.name:Default App}")
    private String appName;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;

    @GetMapping("/config-demo")
    public String showConfig() {
        return String.format(
            "App: %s, Version: %s, Message: %s",
            appName, appVersion, appMessage
        );
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greeting() {
        return "Welcome to " + appName + "! " + appMessage;
    }
}