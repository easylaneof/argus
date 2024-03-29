package ru.tinkoff.edu.parser.uri;

import jakarta.annotation.Nullable;
import java.net.URI;
import ru.tinkoff.edu.parser.ParsingResult;

public abstract class AbstractUriParser implements UriParser {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private final String host;

    public AbstractUriParser(String host) {
        this.host = host;
    }

    @Override
    public final ParsingResult parse(URI uri) {
        if (!isUriOfHost(uri)) {
            return null;
        }

        String path = uri.getPath().replaceAll("/$|^/", ""); // replace first and last slashes
        return parseImpl(path.split("/"));
    }

    protected abstract @Nullable ParsingResult parseImpl(String[] pathParts);

    private boolean isUriOfHost(final URI uri) {
        return host.equalsIgnoreCase(uri.getHost())
               && (HTTP.equals(uri.getScheme()) || HTTPS.equals(uri.getScheme()));
    }
}

