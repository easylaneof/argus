package ru.tinkoff.edu.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;

@RequiredArgsConstructor
@Repository
public class JpaLinkRepository implements LinkRepository {
    private static final String FIND_LEAST_RECENTLY_CHECKED_HQL = """
                    FROM Link l
                    ORDER BY l.lastCheckedAt NULLS FIRST
                    LIMIT :limit
        """;

    private final EntityManager em;

    @Override
    public void save(Link entity) {
        em.persist(entity);
    }

    @Override
    public List<Link> findAll() {
        return em.createQuery("from Link", Link.class).getResultList();
    }

    @Override
    public Optional<Link> findById(Long id) {
        return Optional.ofNullable(em.find(Link.class, id));
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(*) FROM Link", Long.class).getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public void findOrCreate(Link link) {
        Optional<Link> maybeSavedLink = findByUrl(link.getUrl());
        if (maybeSavedLink.isPresent()) {
            Link result = maybeSavedLink.get();

            link.setId(result.getId());
            link.setLastCheckedAt(result.getLastCheckedAt());
            link.setUpdatedAt(result.getUpdatedAt());
            link.setUpdatesCount(result.getUpdatesCount());
            link.setSubscriptions(result.getSubscriptions());
        } else {
            save(link);
        }
    }

    @Override
    public List<Link> findLeastRecentlyChecked(int batchSize) {
        return em.createQuery(FIND_LEAST_RECENTLY_CHECKED_HQL, Link.class)
            .setParameter("limit", batchSize).getResultList();
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        return em.createQuery("from Link l where l.url = :url", Link.class)
            .setParameter("url", url)
            .getResultList()
            .stream()
            .findAny();
    }
}
