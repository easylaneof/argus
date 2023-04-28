package ru.tinkoff.edu.scrapper.client.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class HttpBotUpdateSender implements BotUpdateSender {
    private static final String UPDATES_URI = "/updates";

    private final WebClient client;

    public HttpBotUpdateSender(String url) {
        client = WebClient.create(url);
    }

    @Override
    public void sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        log.info("Sending update to bot by http {}", linkUpdateRequest);

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
