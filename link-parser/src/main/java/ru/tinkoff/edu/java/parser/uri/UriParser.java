package ru.tinkoff.edu.java.parser.uri;

import jakarta.annotation.Nullable;
import ru.tinkoff.edu.java.parser.ParsingResult;

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
