package ru.tinkoff.edu.java.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.uri.UriParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkParserServiceImpl implements LinkParserService {
    private final List<UriParser> parsers;

    @Override
    public Optional<ParsingResult> parse(String url) {
        URI uri;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            return Optional.empty();
        }

        return parsers
                .stream()
                .map(parser -> parser.parse(uri))
                .flatMap(Optional::stream)
                .findFirst();
    }
}
