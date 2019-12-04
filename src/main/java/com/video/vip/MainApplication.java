package com.video.vip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * wxn
 */
@SpringBootApplication
@EnableSwagger2
@Slf4j
public class MainApplication {
    public static void main(String[] args){
        ApplicationContext applicationContext = new SpringApplicationBuilder()
                .sources(MainApplication.class)
                .run(args);
        log.info("项目启动!");
    }
}
