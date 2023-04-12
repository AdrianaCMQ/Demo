package com.tw.oquizfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OQuizFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(OQuizFinalApplication.class, args);
    }

}
