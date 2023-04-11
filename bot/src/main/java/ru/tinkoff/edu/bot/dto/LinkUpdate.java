package ru.tinkoff.edu.bot.dto;

import java.util.List;

public record LinkUpdate(long id, String url, String description, List<Long> tgChatIds) {
}
