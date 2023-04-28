package ru.tinkoff.edu.scrapper.service;

import java.net.URI;
import java.util.List;
import ru.tinkoff.edu.scrapper.entity.Link;

public interface LinkService {
    Link add(long chatId, URI url);

    Link remove(long chatId, URI url);

    List<Link> findChatLinks(long chatId);
}
