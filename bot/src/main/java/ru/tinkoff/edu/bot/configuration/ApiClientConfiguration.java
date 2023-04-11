package ru.tinkoff.edu.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.bot.client.ScrapperClient;
import ru.tinkoff.edu.bot.client.ScrapperClientImpl;

@Configuration
public class ApiClientConfiguration {
    @Bean
    public ScrapperClient scrapperClient(ApplicationProperties applicationProperties) {
        return new ScrapperClientImpl(applicationProperties.api().scrapperApiUrl());
    }
}
