package ru.tinkoff.edu.scrapper.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.scrapper.dto.RemoveLinkRequest;

@RequiredArgsConstructor
@Getter
public class LinkNotFoundException extends RuntimeException {
    private final RemoveLinkRequest removeLinkRequest;
}
