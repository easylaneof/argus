package ru.tinkoff.edu.scrapper.client.bot;

import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HttpBotUpdateSenderTest {
    MockWebServer mockWebServer;

    BotUpdateSender botUpdateSender;

    @BeforeEach
    void setup() {
        mockWebServer = new MockWebServer();
        botUpdateSender = new HttpBotUpdateSender(mockWebServer.url("/").url().toString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 500})
    void sendUpdates__differentResponses_sendsRequest(int statusCode) throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(statusCode));

        botUpdateSender.sendUpdates(linkUpdateRequest());

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/updates");
    }

    private LinkUpdateRequest linkUpdateRequest() {
        return new LinkUpdateRequest(
            123L,
            "",
            "",
            List.of()
        );
    }
}
