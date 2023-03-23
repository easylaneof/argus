package ru.tinkoff.edu.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.scrapper.client.github.Github;
import ru.tinkoff.edu.scrapper.client.stackoverflow.StackOverflow;

@Configuration
@RequiredArgsConstructor
public class ApiClientConfig {
    private final ApiClientProperties apiClientProperties;

    @Bean
    @Github
    public WebClient githubClient() {
        return WebClient.builder()
                .baseUrl(apiClientProperties.githubApiUrl())
                .build();
    }

    @Bean
    @StackOverflow
    public WebClient stackOverflowClient() {
        return WebClient.builder()
                .baseUrl(apiClientProperties.stackOverflowApiUrl())
                .build();
    }

    @Bean
    public long schedulerIntervalMs(ApplicationProperties properties) {
        return properties.scheduler().interval().toMillis();
    }
}
