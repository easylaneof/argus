package ru.tinkoff.edu.scrapper.client.stackoverflow;

import java.util.List;

public record StackOverflowQuestionsResponse(
    List<StackOverflowQuestionResponse> items
) {
}
