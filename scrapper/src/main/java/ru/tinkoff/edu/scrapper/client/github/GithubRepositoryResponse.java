package ru.tinkoff.edu.scrapper.client.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GithubRepositoryResponse(Long id,
                                       @JsonProperty("pushed_at") OffsetDateTime updatedAt) {
}
