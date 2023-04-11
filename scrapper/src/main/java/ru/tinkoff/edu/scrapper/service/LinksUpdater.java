package ru.tinkoff.edu.scrapper.service;

import ru.tinkoff.edu.scrapper.entity.Link;

import java.util.List;

public interface LinksUpdater {
    List<Link> updateLinks(List<Link> links);
}
