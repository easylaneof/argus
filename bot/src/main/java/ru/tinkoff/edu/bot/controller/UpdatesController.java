package ru.tinkoff.edu.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.bot.dto.LinkUpdate;
import ru.tinkoff.edu.bot.service.AlertService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UpdatesController {
    private final AlertService alertService;

    @PostMapping("/updates")
    public void updateLink(@RequestBody LinkUpdate linkUpdate) {
        log.info("Got an update by http {}", linkUpdate);

        alertService.alertAboutUpdate(linkUpdate);
    }
}
