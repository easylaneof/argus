package ru.tinkoff.edu.scrapper.repository;

import ru.tinkoff.edu.scrapper.entity.Link;

import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface LinkRepository extends CrudRepository<Link, Long> {
    void findOrCreate(Link link);

    List<Link> findLeastRecentlyChecked(int batchSize);

    Optional<Link> findByUrl(URI url);
}
