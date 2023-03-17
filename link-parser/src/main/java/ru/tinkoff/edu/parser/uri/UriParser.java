package ru.tinkoff.edu.parser.uri;

import jakarta.annotation.Nullable;
import ru.tinkoff.edu.parser.ParsingResult;

import java.net.URI;
import java.net.URISyntaxException;

public interface UriParser {
    default @Nullable ParsingResult parse(String url) {
        try {
            return parse(new URI(url));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Nullable ParsingResult parse(URI uri);
}
