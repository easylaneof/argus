package ru.tinkoff.edu.scrapper.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DatabaseAccessConfiguration {
    @Configuration
    @ComponentScan("ru.tinkoff.edu.scrapper.repository.jdbc")
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
    @Slf4j
    public static class JdbcAccessConfiguration {
        @PostConstruct
        private void init() {
            log.info("Now using jdbc database access type");
        }
    }

    @Configuration
    @ComponentScan("ru.tinkoff.edu.scrapper.repository.jpa")
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
    @Slf4j
    public static class JpaAccessConfiguration {
        @PostConstruct
        private void init() {
            log.info("Now using jpa database access type");
        }
    }

    @Configuration
    @ComponentScan("ru.tinkoff.edu.scrapper.repository.jooq")
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
    @Slf4j
    public static class JooqAccessConfiguration {
        @PostConstruct
        private void init() {
            log.info("Now using jooq database access type");
        }
    }
}
