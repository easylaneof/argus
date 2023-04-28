package ru.tinkoff.edu.scrapper.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.scrapper.dto.ApiErrorResponse;
import ru.tinkoff.edu.scrapper.exception.LinkNotFoundException;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({LinkNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleLinkNotFound(LinkNotFoundException exception) {
        String requestedLink = exception.getRemoveLinkRequest().link();

        return new ResponseEntity<>(
            new ApiErrorResponse(
                "Requested link %s not found".formatted(requestedLink),
                "0",
                "Link not found",
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toList()
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
                List.of()
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    // TODO: add more exceptions + handlers
}
