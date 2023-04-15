package ru.tinkoff.edu.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.name;
import static ru.tinkoff.edu.scrapper.entity.jooq.tables.Link.LINK;

@RequiredArgsConstructor
@Repository
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext create;

    @Override
    public void save(Link entity) {
        if (entity.getId() != null) {
            long id = entity.getId();

            int rows = create
                    .update(LINK)
                    .set(LINK.LAST_CHECKED_AT, entity.getLastCheckedAt())
                    .set(LINK.UPDATED_AT, entity.getUpdatedAt())
                    .set(LINK.UPDATES_COUNT, entity.getUpdatesCount())
                    .where(LINK.ID.eq(entity.getId()))
                    .execute();

            if (rows == 0) {
                throw new EmptyResultDataAccessException("Expected link with id %s to be stored in db".formatted(id), 0);
            }
        } else {
            Long id = create
                    .insertInto(LINK, LINK.URL)
                    .values(entity.getUrl().toString())
                    .returning(LINK.ID)
                    .fetchOne(LINK.ID);

            entity.setId(id);
        }
    }

    @Override
    public List<Link> findAll() {
        return create
                .select(LINK.ID, LINK.URL, LINK.LAST_CHECKED_AT, LINK.UPDATED_AT, LINK.UPDATES_COUNT)
                .from(LINK)
                .fetchInto(Link.class);
    }

    @Override
    public Optional<Link> findById(Long id) {
        return create
                .select(LINK.ID, LINK.URL, LINK.LAST_CHECKED_AT, LINK.UPDATED_AT, LINK.UPDATES_COUNT)
                .from(LINK)
                .where(LINK.ID.eq(id))
                .fetchOptionalInto(Link.class);
    }

    @Override
    public long count() {
        return create.fetchCount(LINK);
    }

    @Override
    public void deleteById(Long id) {
        create
                .delete(LINK)
                .where(LINK.ID.eq(id))
                .execute();
    }

    @Override
    public void findOrCreate(Link link) {
        var insert = name("insert")
                .as(create
                        .insertInto(LINK, LINK.URL)
                        .values(link.getUrl().toString())
                        .onConflict(LINK.URL).doNothing()
                        .returning(LINK.ID, LINK.URL, LINK.LAST_CHECKED_AT, LINK.UPDATED_AT, LINK.UPDATES_COUNT));

        var select = create
                .select(LINK.ID, LINK.URL, LINK.LAST_CHECKED_AT, LINK.UPDATED_AT, LINK.UPDATES_COUNT)
                .from(LINK)
                .where(LINK.URL.eq(link.getUrl().toString()));

        Link newLink = create
                .with(insert)
                .select()
                .from(insert)
                .union(select)
                .fetchOneInto(Link.class);

        link.setId(newLink.getId());
        link.setLastCheckedAt(newLink.getLastCheckedAt());
        link.setUpdatedAt(newLink.getUpdatedAt());
        link.setUpdatesCount(newLink.getUpdatesCount());
    }

    @Override
    public List<Link> findLeastRecentlyChecked(int batchSize) {
        return create
                .select(LINK.ID, LINK.URL, LINK.LAST_CHECKED_AT, LINK.UPDATED_AT, LINK.UPDATES_COUNT)
                .from(LINK)
                .orderBy(LINK.LAST_CHECKED_AT.asc().nullsFirst())
                .limit(batchSize)
                .fetchInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        return create
                .select(LINK.ID, LINK.URL, LINK.LAST_CHECKED_AT, LINK.UPDATED_AT, LINK.UPDATES_COUNT)
                .from(LINK)
                .where(LINK.URL.eq(url.toString()))
                .fetchOptionalInto(Link.class);
    }
}
