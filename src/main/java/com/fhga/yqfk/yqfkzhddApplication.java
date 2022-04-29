package com.fhga.yqfk;


import org.mybatis.spring.annotation.MapperScan;
import org.quartz.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;




@SpringBootApplication
@ComponentScan("com.fhga.yqfk.controller")
@MapperScan("com.fhga.yqfk.mapper")
public class yqfkzhddApplication {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        SpringApplication.run(yqfkzhddApplication.class, args);
    }

}
