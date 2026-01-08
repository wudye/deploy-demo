package com.mwu.geodistance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeodistanceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GeodistanceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World!");
    }
}
