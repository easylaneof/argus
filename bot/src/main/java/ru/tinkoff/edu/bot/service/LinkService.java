package ru.tinkoff.edu.bot.service;

import ru.tinkoff.edu.bot.dto.LinkResponse;

import java.util.List;
import java.util.Optional;

public interface LinkService {
    Optional<LinkResponse> trackLink(long chatId, String link);

    Optional<LinkResponse> untrackLink(long chatId, String link);

    List<LinkResponse> getAllLinks(long chatId);
}
