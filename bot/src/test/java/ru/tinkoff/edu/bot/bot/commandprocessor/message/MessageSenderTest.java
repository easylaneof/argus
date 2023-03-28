package ru.tinkoff.edu.bot.bot.commandprocessor.message;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.bot.dto.LinkResponse;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MessageSenderTest {
    private static final Faker faker = new Faker(new Random(42));
    private static final Gson gson = new Gson();

    static Configuration templateResolver;

    MessageSender messageSender = new MessageSenderImpl(templateResolver);

    @BeforeAll
    @SneakyThrows
    static void setUp() {
        templateResolver = new Configuration(Configuration.VERSION_2_3_31);

        File templatesDir = new File(MessageSender.class
                .getClassLoader()
                .getResource("templates")
                .getFile()
        );

        templateResolver.setDirectoryForTemplateLoading(templatesDir);
        templateResolver.setDefaultEncoding("UTF-8");
    }

    @Test
    void send__returnsValidMessage() {
        // arrange
        long CHAT_ID = 1L;
        String text = faker.cat().name();

        Update update = makeUpdate(CHAT_ID);

        // act
        SendMessage message = messageSender.send(update, text);

        // assert
        Map<String, Object> result = message.getParameters();

        assertThat(result.get("text")).isEqualTo(text);
        assertThat(result.get("chat_id")).isEqualTo(CHAT_ID);
    }

    @Test
    void sendTemplate__invalidTemplate_throws() {
        // arrange
        long CHAT_ID = 1L;
        Update update = makeUpdate(CHAT_ID);

        // act & assert
        assertThatThrownBy(
                () -> messageSender.sendTemplate(update, "template")
        );
    }

    @Test
    void sendTemplate__start_returnsValidMessage() {
        // arrange
        long CHAT_ID = 1L;
        Update update = makeUpdate(CHAT_ID);

        // act
        SendMessage message = messageSender.sendTemplate(update, "start.ftlh");

        // assert
        Object text = message.getParameters().get("text");

        assertThat(text).isEqualTo("Welcome onboard!");
    }

    @Test
    void sendTemplate__emptyList_returnsValidMessage() {
        // arrange
        long CHAT_ID = 1L;
        Update update = makeUpdate(CHAT_ID);

        // act
        SendMessage message = messageSender.sendTemplate(update, "list.ftlh", Map.of("links", List.of()));

        // assert
        Object text = message.getParameters().get("text");

        assertThat(text).isEqualTo("""
                <b>You don't have any links yet.</b>

                Add some with /track command
                """);
    }

    @Test
    void sendTemplate__nonEmptyList_returnsValidMessage() {
        // arrange
        long CHAT_ID = 1L;

        long FIRST_ID = 10L;
        URI FIRST_URI = URI.create("https://vk.com");

        long SECOND_ID = 20L;
        URI SECOND_URI = URI.create("https://google.com");

        Update update = makeUpdate(CHAT_ID);

        List<LinkResponse> links = List.of(
                new LinkResponse(FIRST_ID, FIRST_URI),
                new LinkResponse(SECOND_ID, SECOND_URI)
        );

        // act
        SendMessage message = messageSender.sendTemplate(update, "list.ftlh", Map.of("links", links));

        // assert
        Object text = message.getParameters().get("text");

        assertThat(text).isEqualTo("""
                <b>Your links:</b>

                %s
                """
                .formatted(links
                        .stream()
                        .map(link -> "<a href=\"%s\">%s</a>".formatted(link.url(), link.url()))
                        .collect(Collectors.joining("\n"))
                )
        );
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