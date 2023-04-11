package ru.tinkoff.edu.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;


@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(@NotNull String test, Scheduler scheduler, Api api) {
    public record Scheduler(Duration interval, int updateBatchSize) {
    }

    public record Api(@DefaultValue("https://api.github.com") String githubApiUrl,
                      @DefaultValue("https://api.stackexchange.com/2.3") String stackOverflowApiUrl,
                      String botApiUrl) {
    }
}
