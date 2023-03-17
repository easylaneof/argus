package ru.tinkoff.edu.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.parser.uri.UriParser;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LinkParserServiceImpl implements LinkParserService {
    private final List<UriParser> parsers;

    @Override
    public ParsingResult parse(String url) {
        return parsers
                .stream()
                .map(parser -> parser.parse(url))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
