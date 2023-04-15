package ru.tinkoff.edu.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcChatRepository implements ChatRepository {
    private static final String SAVE_SQL = """
            INSERT INTO chat(id)
            VALUES (?)
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id FROM chat;
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id FROM chat WHERE id = ?
            """;

    private static final String COUNT_SQL = """
            SELECT COUNT(*) FROM chat
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM chat WHERE id = ?
            """;

    public static final RowMapper<Chat> CHAT_MAPPER = (ResultSet rs, int rowNum) -> new Chat(
            rs.getLong("id")
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Chat entity) {
        jdbcTemplate.update(SAVE_SQL, entity.getId());
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, CHAT_MAPPER);
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return jdbcTemplate.queryForStream(FIND_BY_ID_SQL, CHAT_MAPPER, id).findAny();
    }

    @Override
    public long count() {
        Long res = jdbcTemplate.queryForObject(COUNT_SQL, Long.class);
        return res == null ? 0 : res;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }
}
