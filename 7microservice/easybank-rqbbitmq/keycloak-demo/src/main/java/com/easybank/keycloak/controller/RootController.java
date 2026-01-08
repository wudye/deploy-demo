//package com.easybank.keycloak.controller;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ServerWebExchange;
//
//@RestController
//public class RootController {
//
//    @Value("${app.root-url}")
//    private String rootUrl;
//
//    @Value("${app.home-url}")
//    private String homeUrl;
//
//    @GetMapping("/test")
//    public String test() {
//        return "this is test";
//    }
//
//    @PostMapping("/login")
//    public String login(String name, String password) {
//        return "this is login";
//    }
//
//    @GetMapping("/")
//    public String root(ServerWebExchange exchange) {
//        // 直接重定向到配置的 root-url（可以是内部 /home 或外部地址）
//
//        return "redirect:" + rootUrl;
//    }
//
//    @GetMapping("/home")
//    public String home() {
//        // 返回内部主页视图或再重定向到配置的 home-url
//        return "redirect:" + homeUrl;
//    }
//}
