package ru.tinkoff.edu.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
            INSERT INTO link(url)
            VALUES (?)
            RETURNING id
            """;

    private static final String UPDATE_SQL = """
            UPDATE link
            SET last_checked_at = ?, updated_at = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, url, last_checked_at, updated_at FROM link;
            """;

    private static final String FIND_LEAST_RECENTLY_CHECKED_SQL = """
            SELECT id, url, last_checked_at, updated_at
            FROM link
            ORDER BY last_checked_at NULLS FIRST
            LIMIT ?
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, url, last_checked_at, updated_at FROM link WHERE id = ?
            """;

    private static final String FIND_BY_URL_SQL = """
            SELECT id, url, last_checked_at, updated_at FROM link WHERE url = ?
            """;

    private static final String COUNT_SQL = """
            SELECT COUNT(*) FROM link
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM link WHERE id = ?
            """;

    private static final String FIND_OR_CREATE_SQL = """
            WITH insert AS (
              INSERT INTO link(url)
              VALUES (?)
              ON CONFLICT (url) DO NOTHING
              RETURNING id, url, last_checked_at, updated_at
            )
            SELECT id, url, last_checked_at, updated_at FROM insert
            UNION
            SELECT id, url, last_checked_at, updated_at FROM link
            WHERE url = ?;
            """;
    public static final RowMapper<Link> LINK_MAPPER = (ResultSet rs, int rowNum) -> Link.builder()
            .id(rs.getLong("id"))
            .url(URI.create(rs.getString("url")))
            .lastCheckedAt(rs.getObject("last_checked_at", OffsetDateTime.class))
            .updatedAt(rs.getObject("updated_at", OffsetDateTime.class))
            .build();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Link entity) {
        if (entity.getId() != null) {
            long id = entity.getId();

            int rows = jdbcTemplate.update(UPDATE_SQL, entity.getLastCheckedAt(), entity.getUpdatedAt(), id);

            if (rows == 0) {
                throw new EmptyResultDataAccessException("Expected link with id %s to be stored in db".formatted(id), 0);
            }
        } else {
            Long id = jdbcTemplate.queryForObject(SAVE_SQL, Long.class, entity.getUrl().toString());
            entity.setId(id);
        }
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, LINK_MAPPER);
    }

    @Override
    public List<Link> findLeastRecentlyChecked(int batchSize) {
        return jdbcTemplate.query(FIND_LEAST_RECENTLY_CHECKED_SQL, LINK_MAPPER, batchSize);
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
        String url = link.getUrl().toString();

        Link result = jdbcTemplate.queryForObject(FIND_OR_CREATE_SQL, LINK_MAPPER, url, url);

        link.setId(result.getId());
        link.setLastCheckedAt(result.getLastCheckedAt());
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        return jdbcTemplate.queryForStream(FIND_BY_URL_SQL, LINK_MAPPER, url.toString()).findAny();
    }
}
