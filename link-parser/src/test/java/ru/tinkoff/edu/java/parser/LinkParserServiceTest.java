package ru.tinkoff.edu.java.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.java.parser.uri.UriTestUtils.*;

@SpringBootTest(classes = LinkParserServiceTestConfiguration.class)
public class LinkParserServiceTest {
    @Autowired
    LinkParserService linkParserService;

    @ParameterizedTest
    @MethodSource("provideInvalidLinks")
    void returnsEmptyForInvalidLinks(String invalidLink) {
        assertThat(linkParserService.parse(invalidLink)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideStackOverflowQuestionIds")
    void parsesStackOverflowLink(String link, String questionId) {
        ParsingResult result = linkParserService.parse(link).orElseThrow();

        assertThat(result).isInstanceOf(ParsingResult.StackOverflowQuestion.class);

        var question = (ParsingResult.StackOverflowQuestion) result;

        assertThat(question.id()).isEqualTo(questionId);
    }

    @ParameterizedTest
    @MethodSource("provideGithubUsersAndRepositories")
    void parsesGithubLink(String link, String user, String repositoryName) {
        ParsingResult result = linkParserService.parse(link).orElseThrow();

        assertThat(result).isInstanceOf(ParsingResult.GithubRepository.class);

        var repository = (ParsingResult.GithubRepository) result;

        assertThat(repository.user()).isEqualTo(user);
        assertThat(repository.name()).isEqualTo(repositoryName);
    }

    private static Stream<Arguments> provideInvalidLinks() {
        return Stream.of(
                        "https://google.com",
                        "ftp://github.com",
                        "http://github.com:81",
                        "https://github.com:81",
                        "https://github.com",
                        "https://github.com/settings",
                        "ftp://stackoverflow.com",
                        "http://stackoverflow.com:81",
                        "https://stackoverflow.com:81",
                        "https://stackoverflow.com",
                        "https://stackoverflow.com/123321/mock-link"
                )
                .map(Arguments::of);
    }

    private static Stream<Arguments> provideStackOverflowQuestionIds() {
        return Stream.of("1642028", "111111")
                .map(questionId -> Arguments.of(stackOverflowLink(questionId), questionId));
    }

    private static Stream<Arguments> provideGithubUsersAndRepositories() {
        return Stream.of(
                        new ParsingResult.GithubRepository("sanyarnd", "tinkoff-java-course-2022"),
                        new ParsingResult.GithubRepository("test-user", "test-name")
                )
                .map(r -> Arguments.of(githubLink(r.user(), r.name()), r.user(), r.name()));
    }
}
