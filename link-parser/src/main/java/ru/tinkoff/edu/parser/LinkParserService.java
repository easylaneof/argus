package ru.tinkoff.edu.parser;

import jakarta.annotation.Nullable;

import java.net.URI;
import java.net.URISyntaxException;

public interface LinkParserService {
    default @Nullable ParsingResult parse(String uri) {
        try {
            return parse(new URI(uri));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Nullable
    ParsingResult parse(URI uri);
}
