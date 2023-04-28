package ru.tinkoff.edu.parser;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.parser.uri.UriParser;

@Service
@RequiredArgsConstructor
public class LinkParserServiceImpl implements LinkParserService {
    private final List<UriParser> parsers;

    @Override
    public ParsingResult parse(URI uri) {
        return parsers
            .stream()
            .map(parser -> parser.parse(uri))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }
}
