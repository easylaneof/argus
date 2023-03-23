package ru.tinkoff.edu.java.bot.bot.commandprocessor.util;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommandParserImpl implements CommandParser {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("/(?<command>.*) (?<arguments>.*)");

    @Override
    public Optional<String> parseCommandArgument(String expectedCommand, String message) {
        Matcher matcher = COMMAND_PATTERN.matcher(message);

        if (!matcher.matches() || !matcher.group("command").equals(expectedCommand)) {
            return Optional.empty();
        }

        return Optional.of(matcher.group("arguments"));
    }
}
