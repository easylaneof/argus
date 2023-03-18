package ru.tinkoff.edu.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ApiClientConfig {
    private final ApiClientProperties apiClientProperties;

    @Bean
    public WebClient githubClient() {
        return WebClient.builder()
                .baseUrl(apiClientProperties.githubApiUrl())
                .build();
    }

    @Bean
    public WebClient stackOverflowClient() {
        return WebClient.builder()
                .baseUrl(apiClientProperties.stackOverflowApiUrl())
                .build();
    }
}
