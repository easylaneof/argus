package ru.tinkoff.edu.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.parser.LinkParserService;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LinkServiceImpl implements LinkService {
    private final LinkParserService linkParserService;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public Link add(long chatId, URI url) {
        if (linkParserService.parse(url) == null) {
            throw new RuntimeException("Invalid link"); // TODO: create exception hierarchy
        }

        Link link = makeLink(url);
        subscriptionRepository.addLinkToChat(chatId, link);
        return link;
    }

    @Override
    @Transactional
    public Link remove(long chatId, URI url) {
        if (linkParserService.parse(url) == null) {
            throw new RuntimeException("Invalid link");
        }

        Link link = makeLink(url);
        subscriptionRepository.deleteLinkFromChat(chatId, link);
        return link;
    }

    @Override
    public List<Link> findChatLinks(long chatId) {
        return subscriptionRepository.findChatLinks(chatId);
    }

    private static Link makeLink(URI url) {
        return Link.builder().url(url).build();
    }
}
