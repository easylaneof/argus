package ru.tinkoff.edu.java.parser.uri;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.ParsingResult;

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

        return new ParsingResult.GithubRepository(pathParts[0], pathParts[1]);
    }
}
