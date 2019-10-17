package com.internal.playment.cross;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午5:06
 * Description:
 */
@SpringBootApplication
public class CrossApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CrossApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
