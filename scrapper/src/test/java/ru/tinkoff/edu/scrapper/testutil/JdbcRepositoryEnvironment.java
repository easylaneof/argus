package ru.tinkoff.edu.scrapper.testutil;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcRepositoryEnvironment.JdbcRepositoryTestConfiguration.class)
@JdbcTest
@Rollback
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcRepositoryEnvironment extends IntegrationEnvironment {
    @ComponentScan("ru.tinkoff.edu.scrapper.repository.jdbc")
    @Configuration
    static class JdbcRepositoryTestConfiguration {}
}
