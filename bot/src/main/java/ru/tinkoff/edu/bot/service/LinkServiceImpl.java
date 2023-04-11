package ru.tinkoff.edu.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.client.ScrapperClient;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.dto.ListLinkResponse;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final ScrapperClient scrapperClient;

    @Override
    public Optional<LinkResponse> trackLink(long chatId, String link) {
        return scrapperClient.addLink(chatId, link);
    }

    @Override
    public Optional<LinkResponse> untrackLink(long chatId, String link) {
        return scrapperClient.removeLink(chatId, link);
    }

    @Override
    public List<LinkResponse> getAllLinks(long chatId) {
        return scrapperClient.getAllLinks(chatId)
                .map(ListLinkResponse::links)
                .orElse(List.of());
    }
}
