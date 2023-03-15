package ru.tinkoff.edu.scrapper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.scrapper.dto.LinkResponse;

@RestController
public class TelegramChatController {
    @PostMapping("/tg-chat/{id}")
    public LinkResponse registerChat(@PathVariable long id) {
        return new LinkResponse(0, "");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<LinkResponse> deleteChat(@PathVariable long id) {
        if (id == -1) { // stub
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new LinkResponse(0, ""), HttpStatus.OK);
    }
}
