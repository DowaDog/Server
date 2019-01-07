package com.sopt.dowadog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync// 비동기화를 위해서 추가해준 부분
@SpringBootApplication
@EnableScheduling
public class DowadogApplication {

    public static void main(String[] args) {
        System.setProperty("Asia/Seoul", "UTC");
        SpringApplication.run(DowadogApplication.class, args);
    }
}

