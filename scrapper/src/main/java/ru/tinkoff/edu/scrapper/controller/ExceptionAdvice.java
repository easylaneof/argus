package ru.tinkoff.edu.scrapper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.scrapper.dto.ApiErrorResponse;
import ru.tinkoff.edu.scrapper.exception.LinkNotFoundException;

import java.util.List;

// TODO: move to a shared module?
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({LinkNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleLinkNotFound(LinkNotFoundException exception) {
        String requestedLink = exception.getRemoveLinkRequest().link();

        return new ResponseEntity<>(
                new ApiErrorResponse(
                        "Requested link %s not found".formatted(requestedLink),
                        "0", // TODO: which code?
                        "Link not found",
                        exception.getMessage(),
                        List.of()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({MissingRequestValueException.class})
    public ResponseEntity<ApiErrorResponse> handleValidationException(MissingRequestValueException exception) {
        ProblemDetail detail = exception.getBody();

        return new ResponseEntity<>(
                new ApiErrorResponse(
                        detail.getDetail(),
                        Integer.toString(detail.getStatus()),
                        detail.getTitle(),
                        exception.getMessage(),
                        List.of() // TODO: not sure if we need to return stacktrace (maybe only in dev)
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    // TODO: add more exceptions + handlers
}
