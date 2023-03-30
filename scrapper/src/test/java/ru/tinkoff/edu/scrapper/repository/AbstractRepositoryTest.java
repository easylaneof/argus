package ru.tinkoff.edu.scrapper.repository;

import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.scrapper.testutil.IntegrationEnvironment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AbstractRepositoryTest extends IntegrationEnvironment {
    private static final String TEST_SQL = """
            SELECT table_name
              FROM information_schema.tables
             WHERE table_schema = 'public'
               AND table_type = 'BASE TABLE'
               AND table_name = 'chat';
            """;

    @Test
    void migrationsAreAppliedToTestContainer() throws Exception {
        try (Connection connection = POSTGRES_CONTAINER.createConnection("");
             PreparedStatement statement = connection.prepareStatement(TEST_SQL)) {
            ResultSet resultSet = statement.executeQuery();

            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getString("table_name")).isEqualTo("chat");
        }
    }
}
