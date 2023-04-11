package ru.tinkoff.edu.scrapper.repository.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.scrapper.testutil.JdbcRepositoryEnvironment;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LinkRepositoryTest extends JdbcRepositoryEnvironment {
    private static final URI UNKNOWN_URI = URI.create("google.com");

    private static final long FIRST_LINK_ID = 1L;
    private static final URI FIRST_LINK_URI = URI.create("https://github.com/easylaneof/easylaneof");

    private static final long SECOND_LINK_ID = 2L;
    private static final URI SECOND_LINK_URI = URI.create("https://stackoverflow.com/questions/123321/my-awesome-question");

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
