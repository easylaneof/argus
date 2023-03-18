package ru.tinkoff.edu.scrapper.sheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "${app.scheduler.interval}", timeUnit = TimeUnit.SECONDS)
    public void update() {
        log.info("Scheduling works");
    }
}
