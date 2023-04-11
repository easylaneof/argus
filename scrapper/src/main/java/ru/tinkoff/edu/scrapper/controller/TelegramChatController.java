package ru.tinkoff.edu.scrapper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.scrapper.service.ChatService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TelegramChatController {
    private final ChatService chatService;

    @PostMapping("/tg-chat/{id}")
    public void registerChat(@PathVariable long id) {
        log.info("Registering chat with id {}", id);
        chatService.register(id);
    }

    @DeleteMapping("/tg-chat/{id}")
    public void unregisterChat(@PathVariable long id) {
        log.info("Unregistering chat with id {}", id);
        chatService.unregister(id);
    }
}
