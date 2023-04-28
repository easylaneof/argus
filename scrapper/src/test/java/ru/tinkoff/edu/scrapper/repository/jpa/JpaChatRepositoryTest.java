package ru.tinkoff.edu.scrapper.repository.jpa;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.scrapper.testutil.JpaRepositoryEnvironment;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class JpaChatRepositoryTest extends JpaRepositoryEnvironment {
    private static final long TEST_ID = 123321;

    private static final long FIRST_CHAT_ID = 1;
    private static final long UNKNOWN_CHAT_ID = 1000;

    private static Chat makeTestChat() {
        return new Chat(TEST_ID);
    }

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    EntityManager em;

    @Test
    void findAll__dbIsEmpty_returnsEmptyList() {
        assertThat(chatRepository.findAll()).isEmpty();
    }

    @Test
    @Sql("/sql/add_chats.sql")
    void findAll__dbHasChats_returnsCorrectResult() {
        assertThat(chatRepository.findAll()).hasSize(2);
    }

    @Test
    void findById__dbIsEmpty_returnsEmpty() {
        assertThat(chatRepository.findById(FIRST_CHAT_ID)).isEmpty();
    }

    @Test
    @Sql("/sql/add_chats.sql")
    void findById__dbHasRequiredChat_returnsChat() {
        List<Chat> chats = chatRepository.findAll();

        Chat chat = chats.get(0);
        assertThat(chatRepository.findById(chat.getId())).isNotEmpty();
    }

    @Test
    @Sql("/sql/add_chats.sql")
    void findById__dbDoesntHaveRequiredChat_returnsEmpty() {
        assertThat(chatRepository.findById(UNKNOWN_CHAT_ID)).isEmpty();
    }

    @Test
    void count__dbIsEmpty_returnsZero() {
        assertThat(chatRepository.count()).isEqualTo(0);
    }

    @Test
    @Sql("/sql/add_chats.sql")
    void count__dbHasChats_returnsSize() {
        assertThat(chatRepository.count()).isEqualTo(2);
    }

    @Test
    void save__dbIsEmpty_addsToDb() {
        Chat chat = makeTestChat();

        chatRepository.save(chat);

        Chat foundChat = chatRepository.findAll().get(0);

        assertThat(foundChat.getId()).isEqualTo(chat.getId());
    }

    @Test
    void save__dbAlreadyHasChat_throws() {
        Chat chat = makeTestChat();

        chatRepository.save(chat);

        assertThatThrownBy(() -> {
            chatRepository.save(makeTestChat());
            em.flush();
        }).isInstanceOf(EntityExistsException.class);
    }

    @Test
    @Sql("/sql/add_chats.sql")
    void save__dbHasChats_addsToDb() {
        Chat chat = makeTestChat();

        chatRepository.save(chat);

        assertThat(chat.getId()).isNotNull();
        assertThat(chatRepository.count()).isEqualTo(3);
    }

    @Test
    @Sql("/sql/add_chats.sql")
    void deleteById__dbHasRequiredChat_deletes() {
        Chat chat = chatRepository.findAll().get(0);

        chatRepository.deleteById(chat.getId());

        assertThat(chatRepository.count()).isEqualTo(1);
    }

    @Test
    @Sql("/sql/add_chats.sql")
    void deleteById__dbDoesntHaveRequiredChat_doesNothing() {
        chatRepository.deleteById(UNKNOWN_CHAT_ID);

        assertThat(chatRepository.count()).isEqualTo(2);
    }
}
