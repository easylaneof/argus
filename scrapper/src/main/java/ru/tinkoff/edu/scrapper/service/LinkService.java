package ru.tinkoff.edu.scrapper.service;

import ru.tinkoff.edu.scrapper.entity.Link;

import java.net.URI;
import java.util.List;

public interface LinkService {
    Link add(long chatId, URI url);
    Link remove(long chatId, URI url);
    List<Link> findChatLinks(long chatId);
}
