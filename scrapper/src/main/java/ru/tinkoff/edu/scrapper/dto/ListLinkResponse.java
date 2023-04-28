package ru.tinkoff.edu.scrapper.dto;

import java.util.List;
import ru.tinkoff.edu.scrapper.entity.Link;

public record ListLinkResponse(List<LinkResponse> links, int size) {
    public static ListLinkResponse fromEntity(List<Link> links) {
        return new ListLinkResponse(
            links.stream().map(LinkResponse::fromEntity).toList(),
            links.size()
        );
    }
}
