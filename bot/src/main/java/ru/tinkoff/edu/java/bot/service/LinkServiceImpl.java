package ru.tinkoff.edu.java.bot.service;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.Link;

import java.util.List;
import java.util.Optional;

@Component
public class LinkServiceImpl implements LinkService {
    @Override
    public Optional<Link> trackLink(Long chatId, String link) {
        return Optional.empty();
    }

    @Override
    public Optional<Link> untrackLink(Long chatId, String link) {
        return Optional.empty();
    }

    @Override
    public List<Link> getAllLinks(Long chatId) {
        return List.of();
    }
}
