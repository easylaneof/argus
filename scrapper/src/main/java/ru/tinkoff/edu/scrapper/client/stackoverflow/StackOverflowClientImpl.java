package ru.tinkoff.edu.scrapper.client.stackoverflow;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.parser.ParsingResult.StackOverflowQuestion;

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
    public Optional<StackOverflowQuestionsResponse> checkQuestions(List<StackOverflowQuestion> questions) {
        String ids = questions.stream().map(StackOverflowQuestion::id).collect(Collectors.joining(";"));

        return client
            .get()
            .uri(QUESTION_URI_FORMAT.formatted(ids))
            .retrieve()
            .bodyToMono(StackOverflowQuestionsResponse.class)
            .onErrorResume(WebClientResponseException.class, (ex) -> Mono.empty())
            .blockOptional();
    }
}
