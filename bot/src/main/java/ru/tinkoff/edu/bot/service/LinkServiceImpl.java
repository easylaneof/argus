package ru.tinkoff.edu.bot.service;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.dto.LinkResponse;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Component
public class LinkServiceImpl implements LinkService {
    @Override
    public Optional<LinkResponse> trackLink(Long chatId, String link) {
        return Optional.empty();
    }

    @Override
    public Optional<LinkResponse> untrackLink(Long chatId, String link) {
        return Optional.empty();
    }

    @Override
    public List<LinkResponse> getAllLinks(Long chatId) {
        return List.of(new LinkResponse(1L, URI.create("https://vk.com")), new LinkResponse(2L, URI.create("https://google.com")));
    }
}
