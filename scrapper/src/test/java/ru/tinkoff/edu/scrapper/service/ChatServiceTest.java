package ru.tinkoff.edu.scrapper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    private static final long CHAT_ID = 123312;

    @Mock
    private ChatRepository chatRepository;

    @Captor
    private ArgumentCaptor<Chat> chatCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(chatRepository);
    }

    @Test
    void register__callsRepository() {
        // act
        chatService.register(CHAT_ID);

        // assert
        verify(chatRepository).save(chatCaptor.capture());
        assertThat(chatCaptor.getValue()).isEqualTo(new Chat(CHAT_ID));
    }

    @Test
    void unregister__callsRepository() {
        // act
        chatService.unregister(CHAT_ID);

        // assert
        verify(chatRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(CHAT_ID);
    }
}
