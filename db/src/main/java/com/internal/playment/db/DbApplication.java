package com.internal.playment.db;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午5:05
 * Description:
 */

@SpringBootApplication
public class DbApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DbApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
