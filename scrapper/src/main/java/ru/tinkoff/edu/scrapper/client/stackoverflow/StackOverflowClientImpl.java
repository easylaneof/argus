package ru.tinkoff.edu.scrapper.client.stackoverflow;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.parser.ParsingResult;

import java.util.Optional;

public class StackOverflowClientImpl implements StackOverflowClient {
    private static final String QUESTION_URI_FORMAT = "/questions/%s?site=stackoverflow";

    private static final String DEFAULT_URL = "https://api.stackexchange.com/2.3";

    private final WebClient client;

    public StackOverflowClientImpl(String url) {
        this.client = WebClient.create(url);
    }

    public StackOverflowClientImpl() {
        this.client = WebClient.create(DEFAULT_URL);
    }

    @Override
    public Optional<StackOverflowQuestionResponse> checkQuestion(ParsingResult.StackOverflowQuestion question) {
        return client
                .get()
                .uri(QUESTION_URI_FORMAT.formatted(question.id()))
                .retrieve()
                .bodyToMono(StackOverflowQuestionsResponse.class)
                .onErrorResume(WebClientResponseException.class, (ex) -> Mono.empty())
                .blockOptional()
                .filter(questions -> !questions.items().isEmpty())
                .map(questions -> questions.items().get(0));
    }
}
