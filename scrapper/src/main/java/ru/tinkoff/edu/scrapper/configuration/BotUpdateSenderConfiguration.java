package ru.tinkoff.edu.scrapper.configuration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.scrapper.client.bot.HttpBotUpdateSender;
import ru.tinkoff.edu.scrapper.client.bot.RabbitMQBotUpdateSender;

@Configuration
public class BotUpdateSenderConfiguration {
    @ConditionalOnProperty(prefix = "app", name = "bot-update-sender-type", havingValue = "http")
    @Bean
    public HttpBotUpdateSender httpBotUpdateSender(ApplicationProperties properties) {
        return new HttpBotUpdateSender(properties.api().botApiUrl());
    }

    @ConditionalOnProperty(prefix = "app", name = "bot-update-sender-type", havingValue = "rabbitmq")
    @Bean
    public RabbitMQBotUpdateSender rabbitMQBotUpdateSender(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        return new RabbitMQBotUpdateSender(rabbitTemplate, properties.rabbitMq().topicExchangeName());
    }
}
