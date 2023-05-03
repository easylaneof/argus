package ru.tinkoff.edu.bot.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(
        @NotNull Bot bot,
        @NotNull Api api,
        @NotNull ScrapperQueue scrapperQueue
) {
    record Bot(@NotBlank String apiKey) {
    }

    record Api(@NotBlank String scrapperApiUrl) {
    }

    record ScrapperQueue(@NotBlank String name, @NotBlank String dlqName) {
    }
}
