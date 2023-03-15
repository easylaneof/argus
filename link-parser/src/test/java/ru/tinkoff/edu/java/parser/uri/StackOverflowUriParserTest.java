package ru.tinkoff.edu.java.parser.uri;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.java.parser.ParsingResult;

import java.net.URI;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.java.parser.uri.UriTestUtils.stackOverflowURI;
import static ru.tinkoff.edu.java.parser.uri.UriTestUtils.stackOverflowUriWithTrailingSlash;

public class StackOverflowUriParserTest {
    private final StackOverflowUriParser parser = new StackOverflowUriParser();

    @ParameterizedTest
    @MethodSource("provideInvalidLinks")
    void returnsEmptyForInvalidLinks(URI invalidLink) {
        assertThat(parser.parse(invalidLink)).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideQuestionIds")
    void returnsValidResult(final URI uri, final String questionId) {
        final var result = parser.parse(uri);

        assertThat(result).isInstanceOf(ParsingResult.StackOverflowQuestion.class);

        final var question = (ParsingResult.StackOverflowQuestion) result;

        assertThat(question.id()).isEqualTo(questionId);
    }

    private static Stream<Arguments> provideQuestionIds() {
        return Stream.of("1642028", "111111")
                .flatMap(questionId -> Stream.of(
                        Arguments.of(stackOverflowURI(questionId), questionId),
                        Arguments.of(stackOverflowUriWithTrailingSlash(questionId), questionId)
                ));
    }

    private static Stream<Arguments> provideInvalidLinks() {
        return Stream.of(
                        "https://google.com",
                        "ftp://stackoverflow.com",
                        "http://stackoverflow.com:81",
                        "https://stackoverflow.com:81",
                        "https://stackoverflow.com",
                        "https://stackoverflow.com/123321/mock-link"
                )
                .map(URI::create)
                .map(Arguments::of);
    }
}
