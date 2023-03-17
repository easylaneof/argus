package ru.tinkoff.edu.parser;

import jakarta.annotation.Nullable;

public interface LinkParserService {
    @Nullable ParsingResult parse(String url);
}
