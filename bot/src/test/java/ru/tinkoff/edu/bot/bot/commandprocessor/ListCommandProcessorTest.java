package ru.tinkoff.edu.bot.bot.commandprocessor;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.bot.bot.commandprocessor.message.MessageSender;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.service.LinkService;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCommandProcessorTest {
    private static final Gson gson = new Gson();

    @Mock
    LinkService linkService;

    @Mock
    MessageSender messageSender;

    ListCommandProcessor listCommandProcessor;

    @BeforeEach
    void setup() {
        listCommandProcessor = new ListCommandProcessor(linkService, messageSender);
    }

    @Test
    void command__returnsValidCommand() {
        assertThat(listCommandProcessor.command().command()).isEqualTo("list");
        assertThat(listCommandProcessor.command().description()).isNotEmpty();
    }

    @Test
    void process__callsLinkServiceAndMessageSender() {
        // arrange
        long CHAT_ID = 123;
        List<LinkResponse> links = List.of(new LinkResponse(1L, URI.create("google.com")));

        Update update = makeUpdate(CHAT_ID);

        when(linkService.getAllLinks(CHAT_ID)).thenReturn(links);

        // act
        listCommandProcessor.process(update);

        // assert
        verify(messageSender, times(1))
                .sendTemplate(update, "list.ftlh", Map.of("links", links));

        verify(linkService, times(1)).getAllLinks(CHAT_ID);
    }

    @SneakyThrows
    Update makeUpdate(long chatId) {
        // can't make an instance of Update
        // because Update has no constructor/setters
        return gson.fromJson("""
                {
                    "message": {
                        "chat": {
                            "id": %s
                        }
                    }
                }
                """.formatted(chatId), Update.class);
    }
}
