package ru.tinkoff.edu.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.entity.Subscription;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpaSubscriptionRepository implements SubscriptionRepository {
    private final LinkRepository linkRepository;

    private final EntityManager em;

    @Override
    public List<Link> findChatLinks(long chatId) {
        Chat chat = em.find(Chat.class, chatId);

        if (chat == null) {
            return List.of();
        }

        return chat
                .getSubscriptions()
                .stream()
                .map(Subscription::getLink)
                .toList();
    }

    @Override
    public List<Chat> findLinkChats(URI link) {
        return linkRepository
                .findByUrl(link)
                .map(l -> l
                        .getSubscriptions()
                        .stream()
                        .map(Subscription::getChat)
                        .toList()
                )
                .orElseThrow(RuntimeException::new); // TODO: exception hierarchy
    }

    @Override
    public void addLinkToChat(long chatId, Link link) {
        Chat chat = em.find(Chat.class, chatId);

        if (chat == null) {
            throw new RuntimeException("Create chat before adding link");
        }

        linkRepository.findOrCreate(link);

        Subscription subscription = new Subscription();
        subscription.setChatId(chatId);
        subscription.setLinkId(link.getId());

        chat.addSubscription(subscription);
        link.addSubscription(subscription);

        em.persist(subscription);
    }

    @Override
    public void deleteLinkFromChat(long chatId, Link link) {
        Chat chat = em.find(Chat.class, chatId);

        if (chat == null) {
            throw new RuntimeException("Create chat before deleting link"); // TODO: exception hierarchy
        }

        link = linkRepository.findByUrl(link.getUrl())
                .orElseThrow(() -> new RuntimeException("Can't delete non-existing link"));

        Subscription subscription = em.find(
                Subscription.class,
                new Subscription.SubscriptionId(link.getId(), chatId)
        );

        em.remove(subscription);
    }
}
