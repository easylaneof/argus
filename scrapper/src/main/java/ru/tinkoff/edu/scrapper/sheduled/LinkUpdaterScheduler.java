package ru.tinkoff.edu.scrapper.sheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.scrapper.service.SubscriptionManager;

@Component
@Slf4j
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final SubscriptionManager subscriptionManager;

    @Value("#{updateBatchSize}")
    private int batchSize;

    @Scheduled(fixedDelayString = "#{schedulerIntervalMs}")
    public void update() {
        log.info("Scheduled update");
        subscriptionManager.updateLinks(batchSize);
    }
}
