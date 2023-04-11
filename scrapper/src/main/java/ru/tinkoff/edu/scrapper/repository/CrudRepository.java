package ru.tinkoff.edu.scrapper.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, I> {
    void save(T entity);

    List<T> findAll();

    Optional<T> findById(I id);

    long count();

    void deleteById(I id);
}
