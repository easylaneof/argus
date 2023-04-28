package ru.tinkoff.edu.scrapper.client.stackoverflow;

import java.util.List;
import java.util.Optional;
import ru.tinkoff.edu.parser.ParsingResult.StackOverflowQuestion;

public interface StackOverflowClient {
    default Optional<StackOverflowQuestionResponse> checkQuestion(StackOverflowQuestion question) {
        return checkQuestions(List.of(question))
            .filter(questions -> !questions.items().isEmpty())
            .map(questions -> questions.items().get(0));
    }

    Optional<StackOverflowQuestionsResponse> checkQuestions(List<StackOverflowQuestion> question);
}
