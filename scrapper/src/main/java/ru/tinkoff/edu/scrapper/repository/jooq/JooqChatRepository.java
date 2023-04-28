package ru.tinkoff.edu.scrapper.repository.jooq;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;
import static ru.tinkoff.edu.scrapper.entity.jooq.Tables.CHAT;

@RequiredArgsConstructor
@Repository
public class JooqChatRepository implements ChatRepository {
    private final DSLContext create;

    @Override
    public void save(Chat entity) {
        create
            .insertInto(CHAT)
            .values(entity.getId())
            .execute();
    }

    @Override
    public List<Chat> findAll() {
        return create
            .select(CHAT.ID)
            .from(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return create
            .select(CHAT.ID)
            .from(CHAT)
            .where(CHAT.ID.eq(id))
            .fetchOptionalInto(Chat.class);
    }

    @Override
    public long count() {
        return create
            .fetchCount(CHAT);
    }

    @Override
    public void deleteById(Long id) {
        create
            .delete(CHAT)
            .where(CHAT.ID.eq(id))
            .execute();
    }
}
