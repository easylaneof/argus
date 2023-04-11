package ru.tinkoff.edu.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.scrapper.client.bot.BotClient;
import ru.tinkoff.edu.scrapper.client.bot.LinkUpdateRequest;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BotUpdateNotifierImpl implements BotUpdateNotifier {
    private final BotClient botClient;

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void notifyBot(List<Link> updatedLinks) {
        for (Link link : updatedLinks) {
            List<Long> chatIds = subscriptionRepository
                    .findLinkChats(link.getUrl())
                    .stream()
                    .map(Chat::getId)
                    .toList();

            LinkUpdateRequest request = new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl().toString(),
                    "Something just happened!",
                    chatIds
            );

            botClient.sendUpdates(request);
        }
    }
}
