package ru.tinkoff.edu.java.parser.uri;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.ParsingResult;

import java.util.Optional;

@Component
public class GithubUriParser extends AbstractUriParser {
    private static final String GITHUB_HOST = "github.com";

    public GithubUriParser() {
        super(GITHUB_HOST);
    }

    @Override
    public Optional<ParsingResult> parseImpl(final String[] pathParts) {
        if (pathParts.length != 2) {
            return Optional.empty();
        }

        return Optional.of(new ParsingResult.GithubRepository(pathParts[0], pathParts[1]));
    }
}
