package ru.tinkoff.edu.parser.uri;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.parser.ParsingResult;

import java.net.URI;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GithubUriParserTest {
    private final GithubUriParser parser = new GithubUriParser();

    @ParameterizedTest
    @MethodSource("provideInvalidLinks")
    void returnsEmptyForInvalidLinks(URI invalidLink) {
        assertThat(parser.parse(invalidLink)).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideUsersAndRepositories")
    void returnsValidResult(final URI uri, final String user, final String repository) {
        final var result = parser.parse(uri);

        assertThat(result).isInstanceOf(ParsingResult.GithubRepository.class);

        final var githubRepository = (ParsingResult.GithubRepository) result;

        assertThat(githubRepository.user()).isEqualTo(user);
        assertThat(githubRepository.name()).isEqualTo(repository);
    }

    private static Stream<Arguments> provideUsersAndRepositories() {
        return Stream.of(
                        new ParsingResult.GithubRepository("sanyarnd", "tinkoff-java-course-2022"),
                        new ParsingResult.GithubRepository("test-user", "test-name")
                )
                .flatMap(r -> Stream.of(
                        Arguments.of(UriTestUtils.githubURI(r.user(), r.name()), r.user(), r.name()),
                        Arguments.of(UriTestUtils.githubURIWithTrailingSlash(r.user(), r.name()), r.user(), r.name())
                ));
    }

    private static Stream<Arguments> provideInvalidLinks() {
        return Stream.of(
                        "https://google.com",
                        "ftp://github.com",
                        "http://github.com:81",
                        "https://github.com:81",
                        "https://github.com",
                        "https://github.com/settings"
                )
                .map(URI::create)
                .map(Arguments::of);
    }
}
