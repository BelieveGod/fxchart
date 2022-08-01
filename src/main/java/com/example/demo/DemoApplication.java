package com.example.demo;

import com.example.demo.listenner.AppLifeCycleListenner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoApplication.class);
        springApplication.addListeners(new AppLifeCycleListenner());
        springApplication.run(args);
//        SpringApplication.run(DemoApplication.class, args);
    }

}
