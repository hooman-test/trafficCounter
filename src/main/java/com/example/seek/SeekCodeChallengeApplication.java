package com.example.seek;

import com.example.seek.facade.TrafficLightFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SeekCodeChallengeApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(SeekCodeChallengeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        context.getBean(TrafficLightFacade.class).run();
    }

}
