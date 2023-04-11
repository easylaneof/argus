package ru.tinkoff.edu.scrapper.client.stackoverflow;

import ru.tinkoff.edu.parser.ParsingResult;

import java.util.List;
import java.util.Optional;

public interface StackOverflowClient {
    default Optional<StackOverflowQuestionResponse> checkQuestion(ParsingResult.StackOverflowQuestion question) {
        return checkQuestions(List.of(question))
                .filter(questions -> !questions.items().isEmpty())
                .map(questions -> questions.items().get(0));
    }

    Optional<StackOverflowQuestionsResponse> checkQuestions(List<ParsingResult.StackOverflowQuestion> question);
}
