package ru.tinkoff.edu.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;

import java.net.URI;
import java.util.List;

import static ru.tinkoff.edu.scrapper.entity.jooq.Tables.*;

@RequiredArgsConstructor
@Repository
public class JooqSubscriptionRepository implements SubscriptionRepository {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    private final DSLContext create;

    @Override
    public void addLinkToChat(long chatId, Link link) {
        chatRepository.findById(chatId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Create chat before adding link", 1));

        linkRepository.findOrCreate(link);

        create
                .insertInto(SUBSCRIPTION, SUBSCRIPTION.CHAT_ID, SUBSCRIPTION.LINK_ID)
                .values(chatId, link.getId())
                .execute();
    }

    @Override
    public void deleteLinkFromChat(long chatId, Link link) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Create chat before deleting link", 1));

        link = linkRepository.findByUrl(link.getUrl())
                .orElseThrow(() -> new EmptyResultDataAccessException("Can't delete non-existing link", 1));

        int updatedRows = create
                .delete(SUBSCRIPTION)
                .where(SUBSCRIPTION.CHAT_ID.eq(chat.getId()).and(SUBSCRIPTION.LINK_ID.eq(link.getId())))
                .execute();

        if (updatedRows == 0) {
            throw new EmptyResultDataAccessException("Can't delete link that doesn't belong to chat", 1);
        }
    }

    @Override
    public List<Link> findChatLinks(long chatId) {
        return create
                .select(LINK.ID, LINK.URL, LINK.UPDATED_AT, LINK.LAST_CHECKED_AT, LINK.UPDATES_COUNT)
                .from(LINK)
                .join(SUBSCRIPTION)
                .on(LINK.ID.eq(SUBSCRIPTION.LINK_ID))
                .where(SUBSCRIPTION.CHAT_ID.eq(chatId))
                .fetchInto(Link.class);
    }

    @Override
    public List<Chat> findLinkChats(URI uri) {
        Link link = linkRepository.findByUrl(uri)
                .orElseThrow(() -> new EmptyResultDataAccessException("Create link before searching for chats", 1));

        return create
                .select(CHAT.ID)
                .from(CHAT)
                .join(SUBSCRIPTION)
                .on(CHAT.ID.eq(SUBSCRIPTION.CHAT_ID))
                .where(SUBSCRIPTION.LINK_ID.eq(link.getId()))
                .fetchInto(Chat.class);
    }
}
