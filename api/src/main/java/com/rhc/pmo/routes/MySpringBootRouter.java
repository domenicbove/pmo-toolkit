package com.rhc.pmo.routes;

import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.rhc.pmo.rocketchat.service.RocketChatService;

@SpringBootApplication
@ComponentScan("com.rhc.pmo")
public class MySpringBootRouter extends FatJarRouter {
	
	@Autowired
	RocketChatService rocketChatService;

    @Override
    public void configure() {
        from("timer:trigger")
                .transform().simple("ref:myBean")
                .to("log:out");
    }

    @Bean
    String myBean() {
        return "I'm Spring bean! RocketChat Status is: " +
        		rocketChatService.getRCStatus()	;
    }

}
