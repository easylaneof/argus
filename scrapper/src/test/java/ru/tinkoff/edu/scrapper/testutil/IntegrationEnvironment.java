package ru.tinkoff.edu.scrapper.testutil;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.*;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class IntegrationEnvironment {
    public static PostgreSQLContainer<?> POSTGRES_CONTAINER;

    private static final Path PATH_TO_CHANGE_LOG = new File("migrations").toPath();

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
