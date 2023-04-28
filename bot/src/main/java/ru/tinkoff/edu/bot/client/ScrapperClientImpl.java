package ru.tinkoff.edu.bot.client;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.bot.dto.AddLinkRequest;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.dto.ListLinkResponse;

@Slf4j
public class ScrapperClientImpl implements ScrapperClient {
    private static final String CHATS_URI_FORMAT = "/tg-chat/%s";
    private static final String LINKS_URI = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    private final WebClient scrapperClient;

    public ScrapperClientImpl(String baseUrl) {
        scrapperClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public boolean registerChat(long chatId) {
        return scrapperClient
            .post()
            .uri(CHATS_URI_FORMAT.formatted(chatId))
            .retrieve()
            .toBodilessEntity()
            .onErrorResume(error -> Mono.empty())
            .blockOptional()
            .isPresent();
    }

    @Override
    public boolean deleteChat(long chatId) {
        return scrapperClient
            .delete()
            .uri(CHATS_URI_FORMAT.formatted(chatId))
            .retrieve()
            .toBodilessEntity()
            .onErrorResume(error -> Mono.empty())
            .blockOptional()
            .isPresent();
    }

    @Override
    public Optional<ListLinkResponse> getAllLinks(long chatId) {
        return scrapperClient
            .get()
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, Long.toString(chatId))
            .retrieve()
            .bodyToMono(ListLinkResponse.class)
            .onErrorResume(error -> Mono.empty())
            .blockOptional();
    }

    @Override
    public Optional<LinkResponse> addLink(long chatId, String url) {
        return scrapperClient
            .post()
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, Long.toString(chatId))
            .body(BodyInserters.fromValue(new AddLinkRequest(url)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .onErrorResume(error -> Mono.empty())
            .blockOptional();
    }

    @Override
    public Optional<LinkResponse> removeLink(long chatId, String url) {
        return scrapperClient
            .method(HttpMethod.DELETE) // not .delete() because it can't have body
            // https://stackoverflow.com/questions/60323359/how-to-send-a-body-with-http-delete-when-using-webflux
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, Long.toString(chatId))
            .body(BodyInserters.fromValue(new AddLinkRequest(url)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .onErrorResume(error -> Mono.empty())
            .blockOptional();
    }
}
