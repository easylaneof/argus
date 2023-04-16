package ru.tinkoff.edu.scrapper.client.github;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.parser.ParsingResult.GithubRepository;

import java.util.Optional;

public class GithubClientImpl implements GithubClient {
    private static final String REPOSITORY_URI_FORMAT = "/repos/%s/%s";

    private static final String DEFAULT_URL = "https://api.github.com";

    private final WebClient client;

    public GithubClientImpl(String url) {
        client = WebClient.create(url);
    }

    public GithubClientImpl() {
        client = WebClient.create(DEFAULT_URL);
    }

    @Override
    public Optional<GithubRepositoryResponse> checkRepository(GithubRepository repository) {
        return client
                .get()
                .uri(REPOSITORY_URI_FORMAT.formatted(repository.user(), repository.name()))
                .retrieve()
                .bodyToMono(GithubRepositoryResponse.class)
                .onErrorResume(WebClientResponseException.class, (ex) -> Mono.empty())
                .blockOptional();
    }
}
