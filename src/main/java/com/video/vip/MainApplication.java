package com.video.vip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * wxn
 */
@EnableSwagger2
@SpringBootApplication(exclude = {
        DataSourceTransactionManagerAutoConfiguration.class
        , HibernateJpaAutoConfiguration.class
})
@Slf4j
public class MainApplication {
    public static void main(String[] args){
        ApplicationContext applicationContext = new SpringApplicationBuilder()
                .sources(MainApplication.class)
                .run(args);
        log.info("项目启动!");
    }
}
