package ru.tinkoff.edu.scrapper.service.notifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionManagerImpl implements SubscriptionManager {
    private final LinksUpdater linksUpdater;
    private final LinkRepository linkRepository;
    private final BotUpdateNotifier botUpdateNotifier;

    @Override
    public void updateLinks(int batchSize) {
        List<Link> leastRecentlyChecked = linkRepository.findLeastRecentlyChecked(batchSize);

        log.info("Updating links: {}", leastRecentlyChecked
                .stream()
                .map(Link::getUrl)
                .map(URI::toString)
                .collect(Collectors.joining("\n"))
        );

        List<LinkUpdateDelta> updatedLinks = linksUpdater.updateLinks(leastRecentlyChecked);

        saveLinks(leastRecentlyChecked);

        if (updatedLinks.isEmpty()) {
            log.info("No links updated");
        } else {
            botUpdateNotifier.notifyBot(updatedLinks);
        }
    }

    private void saveLinks(List<Link> links) {
        for (Link link : links) {
            linkRepository.save(link); // TODO: replace with saveAll
        }
    }
}
