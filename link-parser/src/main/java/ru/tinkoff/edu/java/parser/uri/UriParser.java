package ru.tinkoff.edu.java.parser.uri;

import jakarta.annotation.Nullable;
import ru.tinkoff.edu.java.parser.ParsingResult;

import java.net.URI;

public interface UriParser {
    @Nullable ParsingResult parse(URI uri);
}
