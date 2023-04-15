package ru.tinkoff.edu.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;
import ru.tinkoff.edu.scrapper.testutil.JpaRepositoryEnvironment;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class JpaSubscriptionRepositoryTest extends JpaRepositoryEnvironment {
    private static final long TEST_ID = 123321;

    private static final long FIRST_LINK_ID = 1000;
    private static final URI FIRST_LINK_URI = URI.create("https://github.com/easylaneof/easylaneof");
    private static final long SECOND_LINK_ID = 1001;
    private static final URI SECOND_LINK_URI = URI.create("https://stackoverflow.com/questions/123321/my-awesome-question");

    private static final long FIRST_CHAT_ID = 1;
    private static final long SECOND_CHAT_ID = 2;

    private static final URI UNKNOWN_URI = URI.create("https://awesome-unknown-link.com");

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private EntityManager em;

    private static Link makeTestLink() {
        return Link.builder()
                .url(UNKNOWN_URI)
                .build();
    }

    @Test
    void findChatLinks__dbIsEmpty_returnsEmptyList() {
        assertThat(subscriptionRepository.findChatLinks(TEST_ID)).isEmpty();
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void findChatLinks__chatIdIsUnknown_returnsEmptyList() {
        assertThat(subscriptionRepository.findChatLinks(TEST_ID)).isEmpty();
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void findChatLinks__dbHasChats_returnsChatLinks() {
        assertThat(subscriptionRepository.findChatLinks(FIRST_CHAT_ID)).hasSize(2);
        assertThat(subscriptionRepository.findChatLinks(SECOND_CHAT_ID)).hasSize(1);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void findLinkChats__dbHasChats_returnsChatLinks() {
        assertThat(subscriptionRepository.findLinkChats(FIRST_LINK_URI)).hasSize(2);
        assertThat(subscriptionRepository.findLinkChats(SECOND_LINK_URI)).hasSize(1);
    }

    @Test
    void findLinkChats__dbIsEmpty_throws() {
        assertThatThrownBy(() -> subscriptionRepository.findLinkChats(FIRST_LINK_URI))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void findLinkChats__linkIsUnknown_throws() {
        assertThatThrownBy(() -> subscriptionRepository.findLinkChats(UNKNOWN_URI))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void addLinkToChat__dbHasChatsAndLink_addsLink() {
        Link link = linkRepository.findById(SECOND_LINK_ID).orElseThrow();

        subscriptionRepository.addLinkToChat(SECOND_CHAT_ID, link);

        assertThat(subscriptionRepository.findChatLinks(SECOND_CHAT_ID)).hasSize(2);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void addLinkToChat__dbHasChatsButLinkIsNew_addsLink() {
        Link link = makeTestLink();

        subscriptionRepository.addLinkToChat(SECOND_CHAT_ID, link);

        AssertionsForClassTypes.assertThat(linkRepository.findById(link.getId())).isNotEmpty();
        assertThat(subscriptionRepository.findChatLinks(SECOND_CHAT_ID)).hasSize(2);
    }

    @Test
    void addLinkToChat__dbIsEmpty_throws() {
        Link link = makeTestLink();

        assertThatThrownBy(() -> subscriptionRepository.addLinkToChat(FIRST_CHAT_ID, link))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void addLinkToChat__chatAlreadyHasLink_throws() {
        Link link = linkRepository.findAll().get(0);

        assertThatThrownBy(() -> {
            subscriptionRepository.addLinkToChat(FIRST_CHAT_ID, link);
            em.flush();
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void deleteLinkFromChat__dbHasChatsAndLinks_deletes() {
        Link link = linkRepository.findById(FIRST_LINK_ID).orElseThrow();

        subscriptionRepository.deleteLinkFromChat(FIRST_CHAT_ID, link);
        em.flush();

        assertThat(subscriptionRepository.findChatLinks(FIRST_CHAT_ID)).hasSize(1);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void deleteLinkFromChat__dbIsEmpty_throws() {
        Link link = linkRepository.findAll().get(0);

        assertThatThrownBy(() -> subscriptionRepository.addLinkToChat(FIRST_CHAT_ID, link))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void deleteLinkFromChat__chatDoesntHaveLink_throws() {
        Link link = linkRepository.findById(SECOND_LINK_ID).orElseThrow();

        assertThatThrownBy(() -> subscriptionRepository.deleteLinkFromChat(SECOND_CHAT_ID, link))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Sql("/sql/add_subscriptions.sql")
    void deleteLinkFromChat__dbDoesntHaveLink_throws() {
        Link link = makeTestLink();

        assertThatThrownBy(() -> subscriptionRepository.deleteLinkFromChat(SECOND_CHAT_ID, link))
                .isInstanceOf(RuntimeException.class);
    }
}
