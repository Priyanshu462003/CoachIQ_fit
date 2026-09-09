package com.fitness.coachiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoachIQApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoachIQApplication.class, args);
    }
}
