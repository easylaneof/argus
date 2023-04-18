package ru.tinkoff.edu.bot.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.dto.LinkUpdate;
import ru.tinkoff.edu.bot.service.AlertService;

@RabbitListener(queues = "${app.scrapper-queue.name}")
@RequiredArgsConstructor
@Slf4j
@Component
public class ScrapperLinksConsumer {
    private final AlertService alertService;

    @RabbitHandler
    public void receiver(LinkUpdate linkUpdate) {
        log.info("Got an update by RabbitMQ {}", linkUpdate);

        alertService.alertAboutUpdate(linkUpdate);
    }
}
