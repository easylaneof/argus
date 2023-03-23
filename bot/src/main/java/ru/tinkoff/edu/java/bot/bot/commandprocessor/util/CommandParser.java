package ru.tinkoff.edu.java.bot.bot.commandprocessor.util;

import java.util.Optional;

public interface CommandParser {
    Optional<String> parseCommandArgument(String expectedCommand, String message);
}
