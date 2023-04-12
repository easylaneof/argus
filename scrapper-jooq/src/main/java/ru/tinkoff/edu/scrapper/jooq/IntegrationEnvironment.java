package ru.tinkoff.edu.scrapper.jooq;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

@ContextConfiguration(classes = IntegrationEnvironment.IntegrationEnvironmentConfiguration.class)
public abstract class IntegrationEnvironment {
    @Configuration
    static class IntegrationEnvironmentConfiguration {
        @Bean
        public DataSource testDataSource() {
            return DataSourceBuilder.create()
                    .url(POSTGRES_CONTAINER.getJdbcUrl())
                    .username(POSTGRES_CONTAINER.getUsername())
                    .password(POSTGRES_CONTAINER.getPassword())
                    .build();
        }
    }

    public static PostgreSQLContainer<?> POSTGRES_CONTAINER;

    private static final Path PATH_TO_CHANGE_LOG = new File("scrapper/migrations").toPath();

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15.2");
        POSTGRES_CONTAINER.start();

        try (Connection connection = POSTGRES_CONTAINER.createConnection("")) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(PATH_TO_CHANGE_LOG), database);
            liquibase.update();
        } catch (IOException | SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
