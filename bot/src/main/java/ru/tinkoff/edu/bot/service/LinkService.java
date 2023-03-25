package ru.tinkoff.edu.bot.service;

import ru.tinkoff.edu.bot.dto.Link;

import java.util.List;
import java.util.Optional;

public interface LinkService {
    Optional<Link> trackLink(Long chatId, String link);

    Optional<Link> untrackLink(Long chatId, String link);

    List<Link> getAllLinks(Long chatId);
}
