package ru.tinkoff.edu.scrapper.service.notifications;

import ru.tinkoff.edu.scrapper.entity.Link;

import java.util.List;

public interface LinksUpdater {
    record Delta(Link link, String description) {
    }

    List<Delta> updateLinks(List<Link> links);
}
