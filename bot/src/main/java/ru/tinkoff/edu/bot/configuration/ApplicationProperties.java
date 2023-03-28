package ru.tinkoff.edu.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(@NotNull String test, Bot bot, Api api) {
    record Bot(@NotNull String apiKey) {}

    record Api(@NotNull String scrapperApiUrl) {
    }
}
