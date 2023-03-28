package ru.tinkoff.edu.bot.service;

import ru.tinkoff.edu.bot.dto.LinkResponse;

import java.util.List;
import java.util.Optional;

public interface LinkService {
    Optional<LinkResponse> trackLink(Long chatId, String link);

    Optional<LinkResponse> untrackLink(Long chatId, String link);

    List<LinkResponse> getAllLinks(Long chatId);
}
