package ru.tinkoff.edu.scrapper.client.github;

import ru.tinkoff.edu.parser.ParsingResult;

import java.util.Optional;

public interface GithubClient {
    Optional<GithubRepositoryResponse> checkRepository(ParsingResult.GithubRepository repository);
}
