package ru.tinkoff.edu.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.bot.configuration.TestMessageSender;
import ru.tinkoff.edu.bot.dto.LinkUpdate;
import ru.tinkoff.edu.bot.processor.message.MessageSender;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {
    private static final String TEST_URL = "https://github.com/easylaneof/easylaneof";
    private static final String TEST_DESCRIPTION = "New pr in repo";

    @Mock
    TelegramBot bot;

    @Captor
    ArgumentCaptor<SendMessage> sendMessageCaptor;

    MessageSender messageSender = TestMessageSender.getMessageSender();

    AlertService alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertServiceImpl(bot, messageSender);
    }

    @Test
    void alertUpdate__updateIsEmpty_doesNothing() {
        // arrange
        LinkUpdate linkUpdate = linkUpdate(List.of());

        // act
        alertService.alertAboutUpdate(linkUpdate);

        // assert
        verifyNoInteractions(bot);
    }

    @ParameterizedTest
    @MethodSource("provideChatIds")
    void alertUpdate__updateHasSeveralChats_sendsToChat(List<Long> chatIds) {
        // arrange
        LinkUpdate linkUpdate = linkUpdate(chatIds);

        // act
        alertService.alertAboutUpdate(linkUpdate);

        // assert
        verify(bot, times(chatIds.size())).execute(sendMessageCaptor.capture());

        List<SendMessage> values = sendMessageCaptor.getAllValues();

        assertThat(values).hasSameSizeAs(chatIds);

        for (int i = 0; i < chatIds.size(); i++) {
            long chatId = chatIds.get(i);
            SendMessage message = values.get(i);

            assertThat(message.getParameters().get("chat_id")).isEqualTo(chatId);
        }
    }

    @Test
    void alertUpdate__updateHasOneChat_correctContent() {
        // arrange
        LinkUpdate linkUpdate = linkUpdate(List.of(123L));

        // act
        alertService.alertAboutUpdate(linkUpdate);

        // assert
        verify(bot).execute(sendMessageCaptor.capture());

        SendMessage message = sendMessageCaptor.getValue();

        assertThat(message.getParameters().get("text"))
            .isEqualTo("""
                There's an update in your link:

                %s: %s
                """.formatted(TEST_URL, TEST_DESCRIPTION));
    }

    private static Stream<Arguments> provideChatIds() {
        return Stream.of(
            Arguments.of(List.of(1L)),
            Arguments.of(List.of(1L, 2L)),
            Arguments.of(List.of(1L, 2L, 3L, 100L))
        );
    }

    private static LinkUpdate linkUpdate(List<Long> chatIds) {
        return new LinkUpdate(123321, TEST_URL, TEST_DESCRIPTION, chatIds);
    }
}
