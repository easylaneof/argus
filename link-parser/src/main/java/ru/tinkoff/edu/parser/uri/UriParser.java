package ru.tinkoff.edu.parser.uri;

import jakarta.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import ru.tinkoff.edu.parser.ParsingResult;

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
