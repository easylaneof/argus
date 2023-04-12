package ru.tinkoff.edu.scrapper.client.github;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.tinkoff.edu.parser.ParsingResult.GithubRepository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GithubClientTest {
    MockWebServer mockWebServer;

    GithubClient githubClient;

    @BeforeEach
    void setup() {
        mockWebServer = new MockWebServer();
        githubClient = new GithubClientImpl(mockWebServer.url("/").url().toString());
    }

    @ParameterizedTest
    @MethodSource("provideValidResponses")
    void checkRepository__responseIsOk_returnsResponse(GithubRepositoryResponse expected, String user, String repoName) throws InterruptedException {
        mockApiResponse("""
                  {
                      "id": %s,
                      "pushed_at": "%s"
                  }
                """.formatted(expected.id(), expected.updatedAt()));

        GithubRepositoryResponse result = githubClient
                .checkRepository(new GithubRepository(user, repoName))
                .orElseThrow();

        assertThat(result).isEqualTo(expected);

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/repos/%s/%s".formatted(user, repoName));
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 500})
    void checkRepository__responseIsError_returnsEmpty(int code) {
        mockWebServer.enqueue(new MockResponse().setResponseCode(code));

        assertThat(githubClient.checkRepository(new GithubRepository("user", "1"))).isEmpty();
    }

    private void mockApiResponse(String body) {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(body)
        );
    }

    private static Stream<Arguments> provideValidResponses() {
        return Stream.of(
                Arguments.of(
                        new GithubRepositoryResponse(1L,
                                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1662505559), ZoneOffset.UTC),
                                1),
                        "user",
                        "repo-name"
                )
        );
    }
}
