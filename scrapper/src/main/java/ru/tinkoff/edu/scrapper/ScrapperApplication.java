package ru.tinkoff.edu.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.scrapper.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
    public static void main(String[] args) {
        final var ctx = SpringApplication.run(ScrapperApplication.class, args);
        final var config = ctx.getBean(ApplicationConfig.class);
        System.out.println(config);
    }
}
