package ru.tinkoff.edu.java.parser;

import jakarta.annotation.Nullable;

public interface LinkParserService {
    @Nullable ParsingResult parse(String url);
}
