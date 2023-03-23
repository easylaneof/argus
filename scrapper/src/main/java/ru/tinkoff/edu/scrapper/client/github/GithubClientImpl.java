package ru.tinkoff.edu.scrapper.client.github;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.parser.ParsingResult;

import java.util.Optional;

@Component
public class GithubClientImpl implements GithubClient {
    private static final String REPOSITORY_URI_FORMAT = "/repos/%s/%s";

    private final WebClient client;

    public GithubClientImpl(@Github WebClient client) {
        this.client = client;
    }

    @Override
    public Optional<GithubRepositoryResponse> checkRepository(ParsingResult.GithubRepository repository) {
        return client
                .get()
                .uri(REPOSITORY_URI_FORMAT.formatted(repository.user(), repository.name()))
                .retrieve()
                .bodyToMono(GithubRepositoryResponse.class)
                .onErrorResume(WebClientResponseException.class, (ex) -> Mono.empty())
                .blockOptional();
    }
}
