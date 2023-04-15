package ru.tinkoff.edu.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaChatRepository implements ChatRepository {
    private final EntityManager em;

    @Override
    public void save(Chat entity) {
        em.persist(entity);
    }

    @Override
    public List<Chat> findAll() {
        return em.createQuery("from Chat", Chat.class).getResultList();
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return Optional.ofNullable(em.find(Chat.class, id));
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(*) FROM Chat", Long.class).getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
