package ru.tinkoff.edu.bot;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.bot.processor.CommandProcessorFacade;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotUpdatesDispatcherTest {
    private static final Gson gson = new Gson();

    @Mock
    TelegramBot bot;

    @Mock
    CommandProcessorFacade commandProcessorFacade;

    @Mock
    MeterRegistry meterRegistry;

    BotUpdatesDispatcher botUpdatesDispatcher;

    @Captor
    ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void setUp() {
        botUpdatesDispatcher = new BotUpdatesDispatcher(bot, commandProcessorFacade, meterRegistry);
    }

    @Test
    void getCommands__callsCommandProcessorFacade() {
        // arrange
        List<BotCommand> commands = List.of(new BotCommand("test", "test"));

        when(commandProcessorFacade.getCommands()).thenReturn(commands);

        // act & assert
        assertThat(botUpdatesDispatcher.getCommands()).isEqualTo(commands);
    }

    @Test
    void process__unexpectedCommand_returnsHelpMessage() {
        // arrange
        Update update = makeUpdate("unexpected message");

        when(commandProcessorFacade.process(update)).thenReturn(Optional.empty());

        // act
        botUpdatesDispatcher.process(List.of(update));

        verify(bot).execute(sendMessageArgumentCaptor.capture());

        SendMessage message = sendMessageArgumentCaptor.getValue();

        assertThat(message.getParameters().get("text")).isEqualTo(BotUpdatesDispatcher.HELP_MESSAGE);
    }

    // TODO: more tests

    @SneakyThrows
    Update makeUpdate(String message) {
        // can't make an instance of Update
        // because Update has no constructor/setters
        return gson.fromJson("""
            {
                "message": {
                    "chat": {
                        "id": 1
                    },
                    "text": "%s"
                }
            }
            """.formatted(message), Update.class);
    }
}
