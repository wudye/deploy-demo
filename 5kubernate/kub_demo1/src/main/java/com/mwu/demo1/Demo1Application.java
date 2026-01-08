package com.mwu.demo1;

import com.mwu.demo1.domain.Gender;
import com.mwu.demo1.domain.Person;
import com.mwu.demo1.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class Demo1Application implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {


        SpringApplication.run(Demo1Application.class, args);
    }


    @Autowired
    PersonRepository repository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (repository.count() == 0) {
            repository.save(new Person(null, "XXX", "FFF", 20, Gender.MALE));
            repository.save(new Person(null, "AAA", "EEE", 30, Gender.MALE));
            repository.save(new Person(null, "ZZZ", "DDD", 40, Gender.FEMALE));
            repository.save(new Person(null, "BBB", "CCC", 50, Gender.MALE));
            repository.save(new Person(null, "YYY", "JJJ", 60, Gender.FEMALE));
        }
    }
}
