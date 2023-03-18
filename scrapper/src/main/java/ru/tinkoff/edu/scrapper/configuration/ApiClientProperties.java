package ru.tinkoff.edu.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.api")
public record ApiClientProperties(@DefaultValue("https://api.github.com") String githubApiUrl,
                                  @DefaultValue("https://api.stackexchange.com/2.3") String stackOverflowApiUrl) {
}
