package ru.tinkoff.edu.scrapper.service.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.scrapper.client.bot.BotUpdateSender;
import ru.tinkoff.edu.scrapper.client.bot.LinkUpdateRequest;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BotUpdateNotifierImpl implements BotUpdateNotifier {
    private final BotUpdateSender botUpdateSender;

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void notifyBot(List<LinksUpdater.Delta> updatedLinks) {
        for (var delta : updatedLinks) {
            Link link = delta.link();

            List<Long> chatIds = subscriptionRepository
                    .findLinkChats(link.getUrl())
                    .stream()
                    .map(Chat::getId)
                    .toList();

            LinkUpdateRequest request = new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl().toString(),
                    delta.description(),
                    chatIds
            );

            botUpdateSender.sendUpdates(request);
        }
    }
}
