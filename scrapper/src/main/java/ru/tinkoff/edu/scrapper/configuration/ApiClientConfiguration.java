package ru.tinkoff.edu.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.scrapper.client.github.GithubClient;
import ru.tinkoff.edu.scrapper.client.github.GithubClientImpl;
import ru.tinkoff.edu.scrapper.client.stackoverflow.StackOverflowClient;
import ru.tinkoff.edu.scrapper.client.stackoverflow.StackOverflowClientImpl;

@Configuration
public class ApiClientConfiguration {
    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationProperties properties) {
        return new StackOverflowClientImpl(properties.api().stackOverflowApiUrl());
    }

    @Bean
    public GithubClient githubClient(ApplicationProperties properties) {
        return new GithubClientImpl(properties.api().githubApiUrl());
    }
}
