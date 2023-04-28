package ru.tinkoff.edu.scrapper.service;

import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.tinkoff.edu.parser.LinkParserService;
import ru.tinkoff.edu.parser.LinkParserServiceConfiguration;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LinkParserServiceConfiguration.class})
class LinkServiceTest {
    private static final long CHAT_ID = 123312;

    private static final URI TEST_URI = URI.create("https://github.com/easylaneof/easylaneof");
    private static final URI INVALID_URI = URI.create("https://google.com");

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private LinkParserService linkParserService;

    @Captor
    private ArgumentCaptor<Link> linkCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    private LinkService linkService;

    @BeforeEach
    void setUp() {
        linkService = new LinkServiceImpl(linkParserService, subscriptionRepository);
    }

    @Test
    void add__uriIsValid_callsRepository() {
        // act
        linkService.add(CHAT_ID, TEST_URI);

        // assert
        verify(subscriptionRepository).addLinkToChat(idCaptor.capture(), linkCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(CHAT_ID);
        assertThat(linkCaptor.getValue().getUrl()).isEqualTo(TEST_URI);
    }

    @Test
    void add__uriIsInvalid_callsRepository() {
        assertThatThrownBy(() -> linkService.add(CHAT_ID, INVALID_URI));

        verifyNoInteractions(subscriptionRepository);
    }

    @Test
    void remove__uriIsValid_callsRepository() {
        // act
        linkService.remove(CHAT_ID, TEST_URI);

        // assert
        verify(subscriptionRepository).deleteLinkFromChat(idCaptor.capture(), linkCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(CHAT_ID);
        assertThat(linkCaptor.getValue().getUrl()).isEqualTo(TEST_URI);
    }

    @Test
    void remove__uriIsInvalid_callsRepository() {
        assertThatThrownBy(() -> linkService.remove(CHAT_ID, INVALID_URI));

        verifyNoInteractions(subscriptionRepository);
    }

    @Test
    void findChatLinks__uriIsValid_callsRepository() {
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
