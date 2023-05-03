package ru.tinkoff.edu.scrapper.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;


@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(
        @NotNull Scheduler scheduler,
        @NotNull Api api,
        @NotNull DatabaseAccessType databaseAccessType,
        @NotNull BotUpdateSenderType botUpdateSenderType,
        @NotNull RabbitMQ rabbitMq
) {
    public record Scheduler(@NotNull Duration interval, @Positive int updateBatchSize) {
    }

    public record Api(@DefaultValue("https://api.github.com") String githubApiUrl,
                      @DefaultValue("https://api.stackexchange.com/2.3") String stackOverflowApiUrl,
                      @NotBlank String botApiUrl) {
    }

    public record RabbitMQ(@NotBlank String topicExchangeName, @NotBlank String queueName, @NotBlank String linksRoutingKey) {
    }
}
