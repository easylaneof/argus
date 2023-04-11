package ru.tinkoff.edu.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.bot.dto.LinkUpdate;
import ru.tinkoff.edu.bot.service.AlertService;

@RestController
@RequiredArgsConstructor
public class UpdatesController {
    private final AlertService alertService;

    @PostMapping("/updates")
    public void updateLink(@RequestBody LinkUpdate linkUpdate) {
        alertService.alertAboutUpdate(linkUpdate);
    }
}
