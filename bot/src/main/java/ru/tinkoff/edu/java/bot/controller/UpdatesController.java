package ru.tinkoff.edu.java.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

@RestController
public class UpdatesController {
    @PostMapping("/updates")
    public void updateLink(@RequestBody LinkUpdate linkUpdate) {
        // TODO: impl
    }
}
