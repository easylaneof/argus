package ru.tinkoff.edu.scrapper.testutil;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JooqRepositoryEnvironment.JooqRepositoryTestConfiguration.class)
@JooqTest
@Rollback
public class JooqRepositoryEnvironment extends IntegrationEnvironment {
    @ComponentScan("ru.tinkoff.edu.scrapper.repository.jooq")
    @Configuration
    static class JooqRepositoryTestConfiguration {}
}
