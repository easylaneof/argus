package ru.tinkoff.edu.bot.processor.util;

import java.util.Optional;

public interface CommandParser {
    Optional<String> parseCommandArgument(String expectedCommand, String message);
}
