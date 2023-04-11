package ru.tinkoff.edu.scrapper.client.bot;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClientImpl implements BotClient {
    private static final String UPDATES_URI = "/updates";

    private final WebClient client;

    public BotClientImpl(String url) {
        client = WebClient.create(url);
    }

    @Override
    public void sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        client
                .post()
                .uri(UPDATES_URI)
                .body(BodyInserters.fromValue(linkUpdateRequest))
                .retrieve()
                .toBodilessEntity()
                .onErrorResume((ex) -> Mono.empty())
                .block();
    }
}
