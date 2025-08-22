package com.example.quejapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EntityScan(basePackages = {"com.example.quejapp.model"})
@SpringBootApplication
@EnableMongoRepositories
public class QuejappApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuejappApplication.class, args);
    }

}
