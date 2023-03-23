package ru.tinkoff.edu.scrapper.sheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{schedulerIntervalMs}")
    public void update() {
        log.info("Scheduling works");
    }
}
