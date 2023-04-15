package ru.tinkoff.edu.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.parser.LinkParserServiceConfiguration;
import ru.tinkoff.edu.scrapper.configuration.ApplicationProperties;
import ru.tinkoff.edu.scrapper.configuration.DatabaseAccessConfiguration;

/**
 * Disable repositories auto-scan since we use {@link DatabaseAccessConfiguration}
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({ApplicationProperties.class})
@Import(LinkParserServiceConfiguration.class)
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "ru\\.tinkoff\\.edu\\.scrapper\\.repository\\..*")
)
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
