package ru.tinkoff.edu.java.parser.uri;

import ru.tinkoff.edu.java.parser.ParsingResult;

import java.net.URI;
import java.util.Optional;

public abstract class AbstractUriParser implements UriParser {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private final String host;

    public AbstractUriParser(String host) {
        this.host = host;
    }

    @Override
    public final Optional<ParsingResult> parse(URI uri) {
        if (!isUriOfHost(uri)) {
            return Optional.empty();
        }

        String path = uri.getPath();

        var beginsWithSlash = path.startsWith("/");
        var endsWithSlash = path.endsWith("/");

        if (beginsWithSlash || endsWithSlash) {
            int beginIndex = beginsWithSlash ? 1 : 0;
            int endIndex = endsWithSlash ? path.length() - 1 : path.length();
            path = path.substring(beginIndex, endIndex);
        }

        return parseImpl(path.split("/"));
    }

    protected abstract Optional<ParsingResult> parseImpl(String[] pathParts);

    private boolean isUriOfHost(final URI uri) {
        return host.equalsIgnoreCase(uri.getHost()) &&
               (uri.getPort() == -1 || uri.getPort() == 80) &&
               (HTTP.equals(uri.getScheme()) || HTTPS.equals(uri.getScheme()));
    }
}

