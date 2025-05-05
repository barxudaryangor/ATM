package org.example.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AtmApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtmApplication.class, args);
    }


}
