package ru.tinkoff.edu.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrapperConfiguration {
    @Bean
    public long schedulerIntervalMs(ApplicationProperties properties) {
        return properties.scheduler().interval().toMillis();
    }

    @Bean
    public int updateBatchSize(ApplicationProperties properties) {
        return properties.scheduler().updateBatchSize();
    }
}
