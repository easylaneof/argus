package ru.tinkoff.edu.scrapper.client.stackoverflow;

import ru.tinkoff.edu.parser.ParsingResult;

import java.util.Optional;

public interface StackOverflowClient {
    Optional<StackOverflowQuestionResponse> checkQuestion(ParsingResult.StackOverflowQuestion question);
}
