package ru.tinkoff.edu.scrapper.client.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class RabbitMQBotUpdateSender implements BotUpdateSender {
    private final RabbitTemplate rabbitTemplate;
    private final String topicExchangeName;

    @Override
    public void sendUpdates(LinkUpdateRequest request) {
        rabbitTemplate.convertAndSend(
                topicExchangeName,
                "links",
                request
        );
    }
}
