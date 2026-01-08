package com.mwu.kub_demo1;

import com.mwu.demo1.Demo1Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

public class Demo1ApplicationTest {
    public static void main(String[] args) {
        SpringApplication.from(Demo1Application::main)
                .with(MongoDBContainerDevMode.class)
                .run(args);    }
}
