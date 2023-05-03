package ru.tinkoff.edu.bot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.dto.LinkUpdate;

@Slf4j
@Component
@RabbitListener(queues = "${app.scrapper-queue.dlq-name}")
public class DeadLetterQueueConsumer {
    @RabbitHandler
    public void receive(LinkUpdate update) {
        log.info("Failed processing update: {}", update);
    }
}
