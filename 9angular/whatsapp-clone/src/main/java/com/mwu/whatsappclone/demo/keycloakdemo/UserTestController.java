package com.mwu.whatsappclone.demo.keycloakdemo;

import com.mwu.whatsappclone.security.AuthenticatedUser;
import com.mwu.whatsappclone.security.Roles;
import com.mwu.whatsappclone.security.Username;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserTestController {

    @GetMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, World!");
    }

    // ✅ Keycloak 已经自动设置了 Authentication
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        System.out.println("User Attributes: " );

        // 直接从 SecurityContext 获取（Keycloak 已自动设置）
        Username username = AuthenticatedUser.username();
        Roles roles = AuthenticatedUser.roles();
        Map<String, Object> attributes = AuthenticatedUser.attributes();




        System.out.println("User Roles: " + roles.roles());
        System.out.println("User Username: " + username.username());

        return ResponseEntity.ok("hello");
    }

    @PostMapping ("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminOnly() {
        // SecurityContext 中已经有认证信息
        Username username = AuthenticatedUser.username();
        System.out.println("Admin Access by: " + username.username());
        return ResponseEntity.ok("Hello, Admin " + username.username());
    }
}
