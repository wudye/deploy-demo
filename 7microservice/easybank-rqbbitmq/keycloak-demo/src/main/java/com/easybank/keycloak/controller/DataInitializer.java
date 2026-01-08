package com.easybank.keycloak.controller;

import com.easybank.keycloak.entity.User;
import com.easybank.keycloak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有用户，如果没有则创建测试用户
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("test2");
            user.setPassword(passwordEncoder.encode("123456789"));
            user.setEmail("test@example.com");
            user.setRoles("USER,ACCOUNTS");
            user.setEnabled(true);
            userRepository.save(user);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.setRoles("USER,ADMIN,ACCOUNTS,CARDS,LOANS");
            admin.setEnabled(true);
            userRepository.save(admin);
        }
    }
}
