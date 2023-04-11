package ru.tinkoff.edu.scrapper.client.bot;

import java.util.List;

public record LinkUpdateRequest(long id, String url, String description, List<Long> tgChatIds) {
}
