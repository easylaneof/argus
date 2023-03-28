package ru.tinkoff.edu.scrapper.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.scrapper.dto.LinkResponse;

@RestController
public class TelegramChatController {
    @PostMapping("/tg-chat/{id}")
    public void registerChat(@PathVariable long id) {
    }

    @DeleteMapping("/tg-chat/{id}")
    public void deleteChat(@PathVariable long id) {
    }
}
