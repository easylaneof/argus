package ru.tinkoff.edu.scrapper.dto;

import ru.tinkoff.edu.scrapper.entity.Link;

import java.net.URI;

public record LinkResponse(Long id, URI url) {
    public static LinkResponse fromEntity(Link link) {
        return new LinkResponse(link.getId(), link.getUrl());
    }
}
