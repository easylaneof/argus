package ru.tinkoff.edu.scrapper.service.notifications;

import java.util.List;
import ru.tinkoff.edu.scrapper.entity.Link;

public interface LinksUpdater {
    List<LinkUpdateDelta> updateLinks(List<Link> links);
}
