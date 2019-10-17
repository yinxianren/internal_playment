package com.internal.playment.task;


import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TaskApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TaskApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
