package com.easybank.accounts.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class DemoController {
    @GetMapping("/home")
    public String home() {
        return "Hello from Accounts Microservice";
    }


    @PostMapping("/demoPost")
    public String login() {
        return "Hello from Accounts Microservice accounts post demo";
    }
    @PostMapping("/te")
    public String del() {
        return "Hello from Accounts Microservice accounts post demo";
    }
}
