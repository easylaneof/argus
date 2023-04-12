package ru.tinkoff.edu.scrapper.client.stackoverflow;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.tinkoff.edu.parser.ParsingResult.StackOverflowQuestion;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StackOverflowClientTest {
    MockWebServer mockWebServer;

    StackOverflowClient stackOverflowClient;

    @BeforeEach
    void setup() {
        mockWebServer = new MockWebServer();
        stackOverflowClient = new StackOverflowClientImpl(mockWebServer.url("/").url().toString());
    }

    @ParameterizedTest
    @MethodSource("provideValidResponses")
    void checkQuestion__responseIsOk_returnsResponse(StackOverflowQuestionResponse expected) throws InterruptedException {
        mockApiResponse("""
                          {
                              "items": [{"question_id": %s, "last_activity_date": %s, "answer_count": %s}]
                          }
                        """.formatted(expected.id(),
                        expected.updatedAt().toInstant().getLong(ChronoField.INSTANT_SECONDS),
                        expected.answerCount()
                )
        );

        StackOverflowQuestionResponse result = stackOverflowClient
                .checkQuestion(new StackOverflowQuestion(Long.toString(expected.id())))
                .orElseThrow();

        assertThat(result).isEqualTo(expected);

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/questions/%s?site=stackoverflow".formatted(expected.id()));
    }

    @Test
    void checkQuestion__responseAreSeveralQuestions_returnsFirstQuestion() throws InterruptedException {
        final var expectedResult = questions().findFirst().orElseThrow();

        String items = questions()
                .map(question -> "{\"question_id\": %s, \"last_activity_date\": %s, \"answer_count\": %s}".formatted(
                                question.id(),
                                question.updatedAt().toInstant().getLong(ChronoField.INSTANT_SECONDS),
                                question.answerCount()
                        )
                )
                .collect(Collectors.joining(", "));

        mockApiResponse("""
                  {
                      "items": [%s]
                  }
                """.formatted(items));

        StackOverflowQuestionResponse result = stackOverflowClient
                .checkQuestion(new StackOverflowQuestion(Long.toString(expectedResult.id())))
                .orElseThrow();

        assertThat(result).isEqualTo(expectedResult);

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/questions/%s?site=stackoverflow".formatted(expectedResult.id()));
    }

    @Test
    void checkQuestion__responseIsEmpty_returnsEmpty() {
        mockApiResponse("""
                  {
                      "items": []
                  }
                """);

        assertThat(stackOverflowClient.checkQuestion(new StackOverflowQuestion("1"))).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 500})
    void checkQuestion__responseIsError_returnsEmpty(int code) {
        mockWebServer.enqueue(new MockResponse().setResponseCode(code));

        assertThat(stackOverflowClient.checkQuestion(new StackOverflowQuestion("1"))).isEmpty();
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
        return questions().map(Arguments::of);
    }

    private static Stream<StackOverflowQuestionResponse> questions() {
        return Stream.of(
                new StackOverflowQuestionResponse(1L,
                        OffsetDateTime.ofInstant(Instant.ofEpochSecond(1662505559), ZoneOffset.UTC),
                        1),
                new StackOverflowQuestionResponse(123L,
                        OffsetDateTime.ofInstant(Instant.ofEpochSecond(1590400952), ZoneOffset.UTC),
                        1)
        );
    }
}
