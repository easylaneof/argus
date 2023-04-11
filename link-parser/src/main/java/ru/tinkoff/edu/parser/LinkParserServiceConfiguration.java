package ru.tinkoff.edu.parser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.parser.uri.GithubUriParser;
import ru.tinkoff.edu.parser.uri.StackOverflowUriParser;
import ru.tinkoff.edu.parser.uri.UriParser;

import java.util.List;

@Configuration
public class LinkParserServiceConfiguration {
    @Bean
    public GithubUriParser githubUriParser() {
        return new GithubUriParser();
    }

    @Bean
    public StackOverflowUriParser stackOverflowUriParser() {
        return new StackOverflowUriParser();
    }

    @Bean
    public LinkParserService linkParserService(List<UriParser> parsers) {
        return new LinkParserServiceImpl(parsers);
    }
}
