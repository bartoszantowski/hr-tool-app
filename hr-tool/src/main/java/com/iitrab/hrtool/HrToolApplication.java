package com.iitrab.hrtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@SuppressWarnings("NonFinalUtilityClass")
public class HrToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrToolApplication.class, args);
    }

}
