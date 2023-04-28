package ru.tinkoff.edu.scrapper.dto;

import java.net.URI;
import ru.tinkoff.edu.scrapper.entity.Link;

public record LinkResponse(Long id, URI url) {
    public static LinkResponse fromEntity(Link link) {
        return new LinkResponse(link.getId(), link.getUrl());
    }
}
