package com.easybank.keycloak.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TempController {

    @GetMapping("/user")
    public Map<String, Object> user(Authentication authentication) {
        Map<String, Object> userDetails = new HashMap<>();
        if (authentication != null) {
            userDetails.put("name", authentication.getName());
            userDetails.put("authorities", authentication.getAuthorities());
            userDetails.put("principal", authentication.getPrincipal());
        }
        return userDetails;
    }
}
