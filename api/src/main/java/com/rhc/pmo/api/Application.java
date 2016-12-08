package com.rhc.pmo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  
//  @Bean
//  String myBean() {
//      return "I'm Spring bean! RocketChat Status is: " +
//          rocketChatService.getRCStatus() ;
//  }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
