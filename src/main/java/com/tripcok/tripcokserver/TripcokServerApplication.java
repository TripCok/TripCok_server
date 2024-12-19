package com.tripcok.tripcokserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 스케줄링 활성화
public class TripcokServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripcokServerApplication.class, args);
    }

}
