package ru.tinkoff.edu.bot.client;

import java.util.Optional;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.dto.ListLinkResponse;

public interface ScrapperClient {
    boolean registerChat(long chatId);

    boolean deleteChat(long chatId);

    Optional<ListLinkResponse> getAllLinks(long chatId);

    Optional<LinkResponse> addLink(long chatId, String url);

    Optional<LinkResponse> removeLink(long chatId, String url);
}
