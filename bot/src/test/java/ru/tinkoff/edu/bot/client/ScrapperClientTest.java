package ru.tinkoff.edu.bot.client;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.dto.ListLinkResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ScrapperClientTest {
    private static final Faker faker = new Faker(new Random(42));
    private static final Gson gson = new Gson();
    private static final URI FAKE_URI = URI.create("https//github.com/easylaneof/easylaneof");

    MockWebServer mockWebServer;

    ScrapperClient scrapperClient;

    @BeforeEach
    void setUp() {
        mockWebServer = new MockWebServer();
        scrapperClient = new ScrapperClientImpl(mockWebServer.url("/").url().toString());
    }

    @ParameterizedTest
    @MethodSource("provideChatIdsAndResult")
    void registerChat__responseIsSuccessOrNot_returnsIsSuccess(long chatId, boolean isSuccess) {
        enqueueResponse(isSuccess ? 200 : 400);

        boolean result = scrapperClient.registerChat(chatId);

        assertThat(result).isEqualTo(isSuccess);

        assertRequest("POST", "/tg-chat/%s".formatted(chatId));
    }

    @ParameterizedTest
    @MethodSource("provideChatIdsAndResult")
    void deleteChat__responseIsSuccessOrNot_returnsIsSuccess(long chatId, boolean isSuccess) {
        enqueueResponse(isSuccess ? 200 : 400);

        boolean result = scrapperClient.deleteChat(chatId);

        assertThat(result).isEqualTo(isSuccess);

        assertRequest("DELETE", "/tg-chat/%s".formatted(chatId));
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void getAllLinks__responseIsFull_returnsResult(long chatId) {
        ListLinkResponse response = listLinkResponse();

        enqueueResponse(gson.toJson(response));

        ListLinkResponse result = scrapperClient.getAllLinks(chatId).orElseThrow();

        assertThat(result).isEqualTo(response);

        assertRequest("GET", "/links", Map.of("Tg-Chat-Id", Long.toString(chatId)));
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void getAllLinks__responseIsEmpty_returnsResult(long chatId) {
        enqueueResponse("""
            {
              "links": [],
              "size": 0
            }
            """);

        ListLinkResponse result = scrapperClient.getAllLinks(chatId).orElseThrow();

        assertThat(result).isEqualTo(new ListLinkResponse(List.of(), 0));

        assertRequest("GET", "/links", Map.of("Tg-Chat-Id", Long.toString(chatId)));
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void getAllLinks__responseIsInvalid_returnsEmpty(long chatId) {
        enqueueResponse(400);

        assertThat(scrapperClient.getAllLinks(chatId)).isEmpty();

        assertRequest("GET", "/links", Map.of("Tg-Chat-Id", Long.toString(chatId)));
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void addLink__responseIsValid_returnsResult(long chatId) {
        LinkResponse expectedResponse = linkResponse();

        enqueueResponse(gson.toJson(expectedResponse));

        LinkResponse realResponse = scrapperClient.addLink(chatId, FAKE_URI.toString()).orElseThrow();

        assertThat(realResponse).isEqualTo(expectedResponse);

        assertRequest("POST", "/links", Map.of("Tg-Chat-Id", Long.toString(chatId)));
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void addLink__responseIsInvalid_returnsEmpty(long chatId) {
        enqueueResponse(400);

        assertThat(scrapperClient.addLink(chatId, FAKE_URI.toString())).isEmpty();

        assertRequest("POST", "/links", Map.of("Tg-Chat-Id", Long.toString(chatId)));
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void removeLink__responseIsValid_returnsResult(long chatId) {
        LinkResponse expectedResponse = linkResponse();

        enqueueResponse(gson.toJson(expectedResponse));

        LinkResponse realResponse = scrapperClient.removeLink(chatId, FAKE_URI.toString()).orElseThrow();

        assertThat(realResponse).isEqualTo(expectedResponse);

        assertRequest("DELETE", "/links", Map.of("Tg-Chat-Id", Long.toString(chatId)));
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void removeLink__responseIsInvalid_returnsEmpty(long chatId) {
        enqueueResponse(400);

        assertThat(scrapperClient.removeLink(chatId, FAKE_URI.toString())).isEmpty();

        assertRequest("DELETE", "/links", Map.of("Tg-Chat-Id", Long.toString(chatId)));
    }

    @SneakyThrows
    private RecordedRequest assertRequest(String method, String path) {
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);

        return request;
    }

    private RecordedRequest assertRequest(String method, String path, Map<String, String> headers) {
        RecordedRequest request = assertRequest(method, path);

        for (var entry : headers.entrySet()) {
            assertThat(request.getHeader(entry.getKey())).isEqualTo(entry.getValue());
        }

        return request;
    }

    private static Stream<Arguments> provideChatIdsAndResult() {
        return provideChatIds()
            .flatMap(num -> Stream.of(
                Arguments.of(num, true),
                Arguments.of(num, false)
            ));
    }

    private static Stream<Long> provideChatIds() {
        return LongStream.range(0, 5).mapToObj(p -> faker.number().randomNumber());
    }

    private static ListLinkResponse listLinkResponse() {
        return new ListLinkResponse(
            IntStream.range(0, 10).mapToObj(p -> linkResponse()).toList(),
            10
        );
    }

    private static LinkResponse linkResponse() {
        return new LinkResponse(faker.number().randomNumber(), FAKE_URI);
    }

    private void enqueueResponse(int code) {
        mockWebServer.enqueue(new MockResponse().setResponseCode(code));
    }

    private void enqueueResponse(String body) {
        mockWebServer.enqueue(new MockResponse()
            .setBody(body)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200));
    }
}
