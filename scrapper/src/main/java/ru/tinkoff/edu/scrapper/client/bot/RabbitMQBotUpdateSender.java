package ru.tinkoff.edu.scrapper.client.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
@Slf4j
public class RabbitMQBotUpdateSender implements BotUpdateSender {
    private final RabbitTemplate rabbitTemplate;
    private final String topicExchangeName;
    private final String linksRoutingKey;

    @Override
    public void sendUpdates(LinkUpdateRequest request) {
        log.info("Sending update to bot by rabbitmq {}", request);

        rabbitTemplate.convertAndSend(
            topicExchangeName,
            linksRoutingKey,
            request
        );
    }
}
