package ru.tinkoff.edu.scrapper.testutil;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = JpaRepositoryEnvironment.JpaRepositoryTestConfiguration.class)
@DataJpaTest
@EntityScan("ru.tinkoff.edu.scrapper.entity")
@EnableJpaRepositories("ru.tinkoff.edu.scrapper.repository.jpa")
@Rollback
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaRepositoryEnvironment extends IntegrationEnvironment {
    @ComponentScan("ru.tinkoff.edu.scrapper.repository.jpa")
    @Configuration
    static class JpaRepositoryTestConfiguration {}
}
