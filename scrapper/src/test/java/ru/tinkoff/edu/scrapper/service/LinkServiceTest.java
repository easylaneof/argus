package ru.tinkoff.edu.scrapper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {
    private static final long CHAT_ID = 123312;

    private static final URI TEST_URI = URI.create("google.com");

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Captor
    private ArgumentCaptor<Link> linkCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    private LinkService linkService;

    @BeforeEach
    void setUp() {
        linkService = new LinkServiceImpl(subscriptionRepository);
    }

    @Test
    void add__callsRepository() {
        // act
        linkService.add(CHAT_ID, TEST_URI);

        // assert
        verify(subscriptionRepository).addLinkToChat(idCaptor.capture(), linkCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(CHAT_ID);
        assertThat(linkCaptor.getValue().getUrl()).isEqualTo(TEST_URI);
    }

    @Test
    void remove__callsRepository() {
        // act
        linkService.remove(CHAT_ID, TEST_URI);

        // assert
        verify(subscriptionRepository).deleteLinkFromChat(idCaptor.capture(), linkCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(CHAT_ID);
        assertThat(linkCaptor.getValue().getUrl()).isEqualTo(TEST_URI);
    }

    @Test
    void findChatLinks__callsRepository() {
        // arrange
        List<Link> expectedResult = links();
        when(linkService.findChatLinks(CHAT_ID)).thenReturn(expectedResult);

        // act
        List<Link> realResult = linkService.findChatLinks(CHAT_ID);

        // assert
        verify(subscriptionRepository).findChatLinks(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(CHAT_ID);
        assertThat(realResult).isEqualTo(expectedResult);
    }

    private static Link link(long id) {
        return Link.builder()
                .id(id)
                .url(TEST_URI)
                .build();
    }

    private static List<Link> links() {
        return List.of(
                link(1),
                link(100),
                link(312)
        );
    }
}