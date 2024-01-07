package com.example.socialmediapostsscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocialMediaPostsSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaPostsSchedulerApplication.class, args);
    }

}
