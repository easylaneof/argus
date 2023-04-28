package ru.tinkoff.edu.scrapper.repository;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import ru.tinkoff.edu.scrapper.entity.Link;

public interface LinkRepository extends CrudRepository<Link, Long> {
    void findOrCreate(Link link);

    List<Link> findLeastRecentlyChecked(int batchSize);

    Optional<Link> findByUrl(URI url);
}
