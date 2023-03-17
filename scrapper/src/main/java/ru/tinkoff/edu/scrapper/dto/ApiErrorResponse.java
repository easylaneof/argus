package ru.tinkoff.edu.scrapper.dto;

import java.util.List;

// TODO: move to a shared module?
public record ApiErrorResponse(String description, String code, String exceptionName, String exceptionMessage,
                               List<String> stackTrace) {
}
