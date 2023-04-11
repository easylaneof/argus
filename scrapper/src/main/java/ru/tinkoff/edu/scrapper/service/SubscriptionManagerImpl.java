package ru.tinkoff.edu.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionManagerImpl implements SubscriptionManager {
    private final LinksUpdater linksUpdater;
    private final LinkRepository linkRepository;
    private final BotUpdateNotifier botUpdateNotifier;

    @Override
    public void updateLinks(int batchSize) {
        List<Link> leastRecentlyChecked = linkRepository.findLeastRecentlyChecked(batchSize);

        List<Link> updatedLinks = linksUpdater.updateLinks(leastRecentlyChecked);

        saveLinks(leastRecentlyChecked);

        botUpdateNotifier.notifyBot(updatedLinks);
    }

    private void saveLinks(List<Link> links) {
        for (Link link : links) {
            linkRepository.save(link); // TODO: replace with saveAll
        }
    }
}
