package ru.tinkoff.edu.scrapper.repository.jdbc;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.scrapper.testutil.JdbcRepositoryEnvironment;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class JdbcLinkRepositoryTest extends JdbcRepositoryEnvironment {
    private static final URI UNKNOWN_URI = URI.create("google.com");

    private static final long FIRST_LINK_ID = 1L;
    private static final URI FIRST_LINK_URI = URI.create("https://github.com/easylaneof/easylaneof");

    private static final long SECOND_LINK_ID = 2L;
    private static final URI SECOND_LINK_URI =
        URI.create("https://stackoverflow.com/questions/123321/my-awesome-question");

    private static final int BATCH_SIZE = 5;

    private static final long UNKNOWN_ID = 1000L;

    private static Link makeTestLink() {
        return Link.builder().url(UNKNOWN_URI).build();
    }

    @Autowired
    LinkRepository linkRepository;

    @Test
    void findAll__dbIsEmpty_returnsEmptyList() {
        assertThat(linkRepository.findAll()).isEmpty();
    }

    @Test
    @Sql("/sql/add_links.sql")
    void findAll__dbHasLinks_returnsCorrectResult() {
        assertThat(linkRepository.findAll()).hasSize(2);
    }

    @Test
    void findLeastRecentlyChecked__dbIsEmpty_returnsEmptyList() {
        assertThat(linkRepository.findLeastRecentlyChecked(BATCH_SIZE)).isEmpty();
    }

    @Test
    @Sql("/sql/add_links_with_check_time.sql")
    void findLeastRecentlyChecked__dbIsHasLinks_returnsValidResult() {
        List<Link> links = linkRepository.findLeastRecentlyChecked(BATCH_SIZE);

        assertThat(links).hasSize(BATCH_SIZE);

        for (int i = 0; i < 2; i++) {
            assertThat(links.get(i).getLastCheckedAt()).isNull();
        }

        for (int i = 3; i < 5; i++) {
            assertThat(links.get(i).getLastCheckedAt()).isNotNull();
        }
    }

    @Test
    @Sql("/sql/add_links.sql")
    void findById__dbHasRequiredLink_returnsLink() {
        List<Link> links = linkRepository.findAll();

        Link link = links.get(0);
        assertThat(linkRepository.findById(link.getId())).isNotEmpty();
    }

    @Test
    void findById__dbIsEmpty_returnsEmpty() {
        assertThat(linkRepository.findById(FIRST_LINK_ID)).isEmpty();
    }

    @Test
    @Sql("/sql/add_links.sql")
    void findById__dbDoesntHaveRequiredLink_returnsEmpty() {
        assertThat(linkRepository.findById(UNKNOWN_ID)).isEmpty();
    }

    @Test
    @Sql("/sql/add_links.sql")
    void findByUrl__dbHasRequiredLink_returnsLink() {
        assertThat(linkRepository.findByUrl(FIRST_LINK_URI)).isNotEmpty();
        assertThat(linkRepository.findByUrl(SECOND_LINK_URI)).isNotEmpty();
    }

    @Test
    void findByUrl__dbIsEmpty_returnsEmpty() {
        assertThat(linkRepository.findByUrl(FIRST_LINK_URI)).isEmpty();
    }

    @Test
    @Sql("/sql/add_links.sql")
    void findByUrl__dbDoesntHaveRequiredLink_returnsEmpty() {
        assertThat(linkRepository.findByUrl(UNKNOWN_URI)).isEmpty();
    }

    @Test
    void findOrCreate__dbIsEmpty_createsLink() {
        Link link = makeTestLink();

        linkRepository.findOrCreate(link);

        assertThat(link.getId()).isNotNull();
        assertThat(link.getUrl()).isEqualTo(UNKNOWN_URI);

        assertThat(linkRepository.findById(link.getId())).isNotEmpty();
    }

    @Test
    @Sql("/sql/add_links.sql")
    void findOrCreate__dbDoesntHaveRequiredLink_createsLink() {
        Link link = makeTestLink();

        linkRepository.findOrCreate(link);

        assertThat(link.getId()).isNotNull();
        assertThat(link.getUrl()).isEqualTo(UNKNOWN_URI);

        assertThat(linkRepository.findById(link.getId())).isNotEmpty();
    }

    @Test
    void count__dbIsEmpty_returnsZero() {
        assertThat(linkRepository.count()).isEqualTo(0);
    }

    @Test
    @Sql("/sql/add_links.sql")
    void count__dbHasLinks_returnsSize() {
        assertThat(linkRepository.count()).isEqualTo(2);
    }

    @Nested
    class SaveIdIsNull {
        @Test
        void save__dbIsEmpty_addsToDb() {
            Link link = makeTestLink();

            linkRepository.save(link);

            Link foundLink = linkRepository.findAll().get(0);

            assertThat(foundLink.getId()).isEqualTo(link.getId());
            assertThat(foundLink.getUrl()).isEqualTo(link.getUrl());
        }

        @Test
        void save__dbAlreadyHasLink_throws() {
            Link link = makeTestLink();

            linkRepository.save(link);

            assertThatThrownBy(() -> linkRepository.save(makeTestLink()))
                .isInstanceOf(DataAccessException.class);
        }

        @Test
        @Sql("/sql/add_links.sql")
        void save__dbHasLinks_addsToDb() {
            Link link = makeTestLink();

            linkRepository.save(link);

            assertThat(link.getId()).isNotNull();
            assertThat(linkRepository.count()).isEqualTo(3);
        }
    }

    @Nested
    class SaveIdIsNotNull {
        @Test
        void save__idIsUnknownAndDbIsEmpty_throws() {
            Link link = makeTestLink();
            link.setId(UNKNOWN_ID);

            assertThatThrownBy(() -> linkRepository.save(link))
                .isInstanceOf(DataAccessException.class);
        }

        @Test
        @Sql("/sql/add_links.sql")
        void save__idIsUnknownAndDsIsFull_throws() {
            Link link = makeTestLink();
            link.setId(UNKNOWN_ID);

            assertThatThrownBy(() -> linkRepository.save(link))
                .isInstanceOf(DataAccessException.class);
        }

        @Test
        @Sql("/sql/add_links.sql")
        void save__idIsKnownAndDsIsFull_updates() {
            // arrange
            Link link = linkRepository.findAll().get(0);
            long id = link.getId();

            assertThat(link.getLastCheckedAt()).isNull();

            link.setLastCheckedAt(OffsetDateTime.now());

            // act
            linkRepository.save(link);

            //assert
            link = linkRepository.findById(id).orElseThrow();

            assertThat(link.getLastCheckedAt()).isNotNull();
        }
    }

    @Test
    @Sql("/sql/add_links.sql")
    void deleteById__dbHasRequiredLink_deletes() {
        Link link = linkRepository.findAll().get(0);

        linkRepository.deleteById(link.getId());

        assertThat(linkRepository.count()).isEqualTo(1);
    }

    @Test
    @Sql("/sql/add_links.sql")
    void deleteById__dbDoesntHaveRequiredLink_doesNothing() {
        linkRepository.deleteById(UNKNOWN_ID);

        assertThat(linkRepository.count()).isEqualTo(2);
    }
}
