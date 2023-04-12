package ru.tinkoff.edu.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.entity.Link;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.scrapper.repository.SubscriptionRepository;

import java.net.URI;
import java.util.List;

import static ru.tinkoff.edu.scrapper.repository.jdbc.JdbcChatRepository.CHAT_MAPPER;
import static ru.tinkoff.edu.scrapper.repository.jdbc.JdbcLinkRepository.LINK_MAPPER;

@RequiredArgsConstructor
@Repository
@Primary // TODO: replace with configuration
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private static final String ADD_LINK_TO_CHAT_SQL = """
            INSERT INTO subscription(chat_id, link_id) VALUES (?, ?);
            """;

    private static final String DELETE_LINK_FROM_CHAT_SQL = """
            DELETE FROM subscription WHERE chat_id = ? AND link_id = ?;
            """;

    private static final String FIND_CHAT_LINKS_SQL = """
            SELECT id, url, last_checked_at, updated_at, updates_count
            FROM link l
            JOIN subscription s ON l.id = s.link_id
            WHERE s.chat_id = ?
            """;

    private static final String FIND_LINK_CHATS_SQL = """
            SELECT id
            FROM chat c
            JOIN subscription s ON c.id = s.chat_id
            WHERE s.link_id = ?
            """;

    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLinkToChat(long chatId, Link link) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Create chat before adding link", 1));

        linkRepository.findOrCreate(link);

        jdbcTemplate.update(ADD_LINK_TO_CHAT_SQL, chat.getId(), link.getId());
    }

    @Override
    public void deleteLinkFromChat(long chatId, Link link) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Create chat before deleting link", 1));

        link = linkRepository.findByUrl(link.getUrl())
                .orElseThrow(() -> new EmptyResultDataAccessException("Can't delete non-existing link", 1));

        int updatedRows = jdbcTemplate.update(DELETE_LINK_FROM_CHAT_SQL, chat.getId(), link.getId());

        if (updatedRows == 0) {
            throw new EmptyResultDataAccessException("Can't delete link that doesn't belong to chat", 1);
        }
    }

    @Override
    public List<Link> findChatLinks(long chatId) {
        return jdbcTemplate.query(FIND_CHAT_LINKS_SQL, LINK_MAPPER, chatId);
    }

    @Override
    public List<Chat> findLinkChats(URI uri) {
        Link link = linkRepository.findByUrl(uri)
                .orElseThrow(() -> new EmptyResultDataAccessException("Create link before searching for chats", 1));

        return jdbcTemplate.query(FIND_LINK_CHATS_SQL, CHAT_MAPPER, link.getId());
    }
}
