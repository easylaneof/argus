package ru.tinkoff.edu.java.parser.uri;

import ru.tinkoff.edu.java.parser.ParsingResult;

import java.net.URI;
import java.util.Optional;

public interface UriParser {
    Optional<ParsingResult> parse(URI uri);
}
