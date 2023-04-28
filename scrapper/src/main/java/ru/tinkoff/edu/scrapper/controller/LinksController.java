package ru.tinkoff.edu.scrapper.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.scrapper.dto.ListLinkResponse;
import ru.tinkoff.edu.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.scrapper.service.LinkService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LinksController {
    private final LinkService linkService;

    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @GetMapping("/links")
    public ListLinkResponse getAllLinks(@RequestHeader(TG_CHAT_ID_HEADER) long chatId) {
        log.info("List all links of chat {}", chatId);
        return ListLinkResponse.fromEntity(linkService.findChatLinks(chatId));
    }

    @PostMapping("/links")
    public LinkResponse addLink(
        @RequestHeader(TG_CHAT_ID_HEADER) long chatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        log.info("Adding new link {} to chat {}", addLinkRequest, chatId);
        return LinkResponse.fromEntity(linkService.add(chatId, URI.create(addLinkRequest.link())));
    }

    @DeleteMapping("/links")
    public LinkResponse removeLink(
        @RequestHeader(TG_CHAT_ID_HEADER) long chatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        log.info("Removing link {} from chat {}", removeLinkRequest, chatId);
        return LinkResponse.fromEntity(linkService.remove(chatId, URI.create(removeLinkRequest.link())));
    }
}
