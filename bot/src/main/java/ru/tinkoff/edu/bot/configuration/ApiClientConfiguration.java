package ru.tinkoff.edu.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiClientConfiguration {
    @Bean
    public WebClient scrapperClient(ApplicationProperties applicationProperties) {
        return WebClient.builder()
                .baseUrl(applicationProperties.api().scrapperApiUrl())
                .build();
    }
}
