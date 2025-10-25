package com.zayden_project.addFriend_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AddFriendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddFriendServiceApplication.class, args);
    }
}
