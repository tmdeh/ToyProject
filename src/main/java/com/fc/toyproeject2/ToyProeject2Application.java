package com.fc.toyproeject2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ToyProeject2Application {

    public static void main(String[] args) {
        SpringApplication.run(ToyProeject2Application.class, args);
    }

}
