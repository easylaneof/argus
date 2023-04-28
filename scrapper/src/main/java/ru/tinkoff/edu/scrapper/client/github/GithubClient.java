package ru.tinkoff.edu.scrapper.client.github;

import java.util.Optional;
import ru.tinkoff.edu.parser.ParsingResult.GithubRepository;

public interface GithubClient {
    Optional<GithubRepositoryResponse> checkRepository(GithubRepository repository);
}
