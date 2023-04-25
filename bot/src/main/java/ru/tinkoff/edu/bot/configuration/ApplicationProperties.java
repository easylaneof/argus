package ru.tinkoff.edu.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(
        Bot bot,
        Api api,
        ScrapperQueue scrapperQueue
) {
    record Bot(@NotNull String apiKey) {
    }

    record Api(@NotNull String scrapperApiUrl) {
    }

    record ScrapperQueue(String name, String dlqName) {
    }
}
