package ru.tinkoff.edu.parser.uri;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.parser.ParsingResult;
import ru.tinkoff.edu.parser.ParsingResult.GithubRepository;

@Component
public class GithubUriParser extends AbstractUriParser {
    private static final String GITHUB_HOST = "github.com";

    public GithubUriParser() {
        super(GITHUB_HOST);
    }

    @Override
    public ParsingResult parseImpl(final String[] pathParts) {
        if (pathParts.length != 2) {
            return null;
        }

        return new GithubRepository(pathParts[0], pathParts[1]);
    }
}
