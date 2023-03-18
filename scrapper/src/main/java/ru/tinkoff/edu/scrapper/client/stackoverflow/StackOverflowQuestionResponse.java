package ru.tinkoff.edu.scrapper.client.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackOverflowQuestionResponse(@JsonProperty("question_id") Long id,
                                            @JsonProperty("last_activity_date") OffsetDateTime updatedAt) {
}
