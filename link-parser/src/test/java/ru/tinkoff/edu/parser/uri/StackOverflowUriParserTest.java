package ru.tinkoff.edu.parser.uri;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.parser.ParsingResult.StackOverflowQuestion;

import java.net.URI;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StackOverflowUriParserTest {
    private final StackOverflowUriParser parser = new StackOverflowUriParser();

    @ParameterizedTest
    @MethodSource("provideInvalidLinks")
    void parse__linkIsInvalid_returnsEmpty(URI invalidLink) {
        assertThat(parser.parse(invalidLink)).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideQuestionIds")
    void parse__linkIsValid_returnsQuestion(final URI uri, final String questionId) {
        final var result = parser.parse(uri);

        assertThat(result).isInstanceOf(StackOverflowQuestion.class);

        final var question = (StackOverflowQuestion) result;

        assertThat(question.id()).isEqualTo(questionId);
    }

    private static Stream<Arguments> provideQuestionIds() {
        return Stream.of("1642028", "111111")
                .flatMap(questionId -> Stream.of(
                        Arguments.of(UriTestUtils.stackOverflowURI(questionId), questionId),
                        Arguments.of(UriTestUtils.stackOverflowUriWithTrailingSlash(questionId), questionId)
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
