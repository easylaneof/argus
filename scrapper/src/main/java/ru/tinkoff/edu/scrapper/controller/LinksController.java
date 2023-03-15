package ru.tinkoff.edu.scrapper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.scrapper.dto.ListLinkResponse;

import java.util.List;

@RestController
public class LinksController {
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @GetMapping("/links")
    public ListLinkResponse getAllLinks(@RequestHeader(TG_CHAT_ID_HEADER) long chatId) {
        return new ListLinkResponse(List.of(), 0);
    }

    @PostMapping("/links")
    public LinkResponse addLink(@RequestHeader(TG_CHAT_ID_HEADER) long chatId, @RequestBody AddLinkRequest addLinkRequest) {
        return new LinkResponse(0, addLinkRequest.link());
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(@RequestHeader(TG_CHAT_ID_HEADER) long chatId, @RequestBody AddLinkRequest addLinkRequest) {
        if (chatId == -1) { // stub
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new LinkResponse(0, addLinkRequest.link()), HttpStatus.OK);
    }
}
