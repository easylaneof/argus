package ru.tinkoff.edu.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;

import java.net.URI;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private static final String SAVE_SQL = """
            INSERT INTO link(url, updated_at)
            VALUES (?, ?)
            RETURNING id
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, url, updated_at FROM link;
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, url, updated_at FROM link WHERE id = ?
            """;

    private static final String FIND_BY_URL = """
            SELECT id, url, updated_at FROM link WHERE url = ?
            """;

    private static final String COUNT_SQL = """
            SELECT COUNT(*) FROM link
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM link WHERE id = ?
            """;

    private static final String FIND_OR_CREATE_SQL = """
            WITH insert AS (
              INSERT INTO link(url, updated_at)
              VALUES (?, ?)
              ON CONFLICT (url) DO NOTHING
              RETURNING id
            )
            SELECT id FROM insert
            UNION
            SELECT id FROM link
            WHERE url = ?;
            """;
    public static final RowMapper<Link> LINK_MAPPER = (ResultSet rs, int rowNum) -> Link.builder()
            .id(rs.getLong("id"))
            .url(URI.create(rs.getString("url")))
            .updatedAt(rs.getObject("updated_at", OffsetDateTime.class))
            .build();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Link entity) {
        OffsetDateTime updatedAt = entity.getUpdatedAt() == null ? OffsetDateTime.now() : entity.getUpdatedAt();

        Long id = jdbcTemplate.queryForObject(SAVE_SQL, Long.class, entity.getUrl().toString(), updatedAt);
        entity.setId(id);
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, LINK_MAPPER);
    }

    @Override
    public Optional<Link> findById(Long id) {
        return jdbcTemplate.queryForStream(FIND_BY_ID_SQL, LINK_MAPPER, id).findAny();
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

    @Override
    public void findOrCreate(Link link) {
        OffsetDateTime updatedAt = link.getUpdatedAt() == null ? OffsetDateTime.now() : link.getUpdatedAt();
        link.setUpdatedAt(updatedAt);
        String url = link.getUrl().toString();

        Long id = jdbcTemplate.queryForObject(FIND_OR_CREATE_SQL, Long.class, url, updatedAt, url);

        link.setId(id);
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        return jdbcTemplate.queryForStream(FIND_BY_URL, LINK_MAPPER, url.toString()).findAny();
    }
}
