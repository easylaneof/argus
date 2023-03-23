package ru.tinkoff.edu.scrapper.client.stackoverflow;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.parser.ParsingResult;

import java.util.Optional;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {
    private static final String QUESTION_URI_FORMAT = "/questions/%s?site=stackoverflow";

    private final WebClient client;

    public StackOverflowClientImpl(@StackOverflow WebClient client) {
        this.client = client;
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
