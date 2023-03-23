package ru.tinkoff.edu.java.bot.bot.commandprocessor.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommandParserTest {
    private static final String TRACK = "track";
    private static final String UNTRACK = "untrack";
    private static final String HELP = "help";
    private static final String TRACK_COMMAND = "/" + TRACK;
    private static final String UNTRACK_COMMAND = "/" + UNTRACK;
    private static final String HELP_COMMAND = "/" + HELP;
    private static final String MOCK_URL = "https://github.com/easylaneof/easylaneof";

    private final CommandParser commandParser = new CommandParserImpl();

    @ParameterizedTest
    @MethodSource("provideCommands")
    void returnsValidCommandAndArguments(String message, String command, String arguments) {
        String result = commandParser.parseCommandArgument(command, message).orElseThrow();

        assertThat(result).isEqualTo(arguments);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMessage")
    void returnsInvalidCommandAndArguments(String message, String command) {
        assertThat(commandParser.parseCommandArgument(command, message)).isEmpty();
    }

    private static Stream<Arguments> provideCommands() {
        return Stream.of(
                Arguments.of(TRACK_COMMAND + " " + MOCK_URL, TRACK, MOCK_URL),
                Arguments.of(UNTRACK_COMMAND + " " + MOCK_URL, UNTRACK, MOCK_URL)
        );
    }

    private static Stream<Arguments> provideInvalidMessage() {
        return Stream.of(
                Arguments.of(HELP_COMMAND, HELP),
                Arguments.of(HELP_COMMAND, TRACK),
                Arguments.of(TRACK_COMMAND + " " + MOCK_URL, UNTRACK),
                Arguments.of(UNTRACK_COMMAND + " " + MOCK_URL, TRACK),
                Arguments.of(TRACK_COMMAND + /* no space */ MOCK_URL, TRACK),
                Arguments.of(UNTRACK_COMMAND + /* no space */ MOCK_URL, TRACK),
                Arguments.of(/* no slash */ TRACK + " " + MOCK_URL, TRACK),
                Arguments.of(/* no slash */ UNTRACK + " " + MOCK_URL, TRACK),
                Arguments.of(/* no slash */ TRACK + /* no space */ MOCK_URL, TRACK),
                Arguments.of(/* no slash */ UNTRACK + /* no space */ MOCK_URL, TRACK)
        );
    }
}
