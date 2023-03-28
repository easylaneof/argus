package ru.tinkoff.edu.bot.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.bot.dto.AddLinkRequest;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.dto.ListLinkResponse;

import java.util.Optional;

@RequiredArgsConstructor
public class ScrapperClientImpl implements ScrapperClient {
    private static final String CHATS_URI_FORMAT = "/tg-chat/%s";
    private static final String LINKS_URI_FORMAT = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    private final WebClient scrapperClient;

    @Override
    public boolean registerChat(long chatId) {
        return Boolean.TRUE.equals(scrapperClient
                .post()
                .uri(CHATS_URI_FORMAT.formatted(chatId))
                .retrieve()
                .toBodilessEntity()
                .flatMap((clientResponse) -> Mono.just(!clientResponse.getStatusCode().isError()))
                .block());
    }

    @Override
    public boolean deleteChat(long chatId) {
        return Boolean.TRUE.equals(scrapperClient
                .delete()
                .uri(CHATS_URI_FORMAT.formatted(chatId))
                .retrieve()
                .toBodilessEntity()
                .flatMap((clientResponse) -> Mono.just(!clientResponse.getStatusCode().isError()))
                .block());
    }

    @Override
    public Optional<ListLinkResponse> getAllLinks(long chatId) {
        return scrapperClient
                .get()
                .uri(LINKS_URI_FORMAT)
                .header(TG_CHAT_ID_HEADER, Long.toString(chatId))
                .retrieve()
                .bodyToMono(ListLinkResponse.class)
                .onErrorResume(WebClientResponseException.class, (ex) -> Mono.empty())
                .blockOptional();
    }

    @Override
    public Optional<LinkResponse> addLink(long chatId, String url) {
        return scrapperClient
                .post()
                .uri(LINKS_URI_FORMAT)
                .header(TG_CHAT_ID_HEADER, Long.toString(chatId))
                .body(BodyInserters.fromValue(new AddLinkRequest(url)))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .onErrorResume(WebClientResponseException.class, (ex) -> Mono.empty())
                .blockOptional();
    }

    @Override
    public Optional<LinkResponse> removeLink(long chatId, String url) {
        return scrapperClient
                .method(HttpMethod.DELETE) // not .delete() because it can't have body
                // https://stackoverflow.com/questions/60323359/how-to-send-a-body-with-http-delete-when-using-webflux
                .uri(LINKS_URI_FORMAT)
                .header(TG_CHAT_ID_HEADER, Long.toString(chatId))
                .body(BodyInserters.fromValue(new AddLinkRequest(url)))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .onErrorResume(WebClientResponseException.class, (ex) -> Mono.empty())
                .blockOptional();
    }
}
