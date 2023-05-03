package ru.tinkoff.edu.scrapper.service.notifications;

import ru.tinkoff.edu.scrapper.entity.Link;

import java.util.List;

public interface LinksUpdater {
    List<LinkUpdateDelta> updateLinks(List<Link> links);
}
