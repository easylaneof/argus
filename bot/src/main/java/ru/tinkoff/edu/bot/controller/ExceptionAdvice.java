package ru.tinkoff.edu.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.bot.dto.ApiErrorResponse;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({MissingRequestValueException.class})
    public ResponseEntity<ApiErrorResponse> handleValidationException(MissingRequestValueException exception) {
        ProblemDetail detail = exception.getBody();

        return new ResponseEntity<>(
                new ApiErrorResponse(
                        detail.getDetail(),
                        Integer.toString(detail.getStatus()),
                        detail.getTitle(),
                        exception.getMessage(),
                        Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    // TODO: add more exceptions + handlers
}
