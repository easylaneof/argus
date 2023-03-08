package ru.tinkoff.edu.java.parser;

import java.util.Optional;

public interface LinkParserService {
    Optional<ParsingResult> parse(String url);
}
