package ru.tinkoff.edu.scrapper.repository;

import java.net.URI;
import java.util.List;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.entity.Link;

public interface SubscriptionRepository {
    List<Link> findChatLinks(long chatId);

    List<Chat> findLinkChats(URI link);

    void addLinkToChat(long chatId, Link link);

    void deleteLinkFromChat(long chatId, Link link);
}
