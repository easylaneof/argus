package ru.tinkoff.edu.scrapper.service.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.parser.LinkParserService;
import ru.tinkoff.edu.parser.ParsingResult;
import ru.tinkoff.edu.parser.ParsingResult.GithubRepository;
import ru.tinkoff.edu.parser.ParsingResult.StackOverflowQuestion;
import ru.tinkoff.edu.scrapper.client.github.GithubClient;
import ru.tinkoff.edu.scrapper.client.github.GithubRepositoryResponse;
import ru.tinkoff.edu.scrapper.client.stackoverflow.StackOverflowClient;
import ru.tinkoff.edu.scrapper.client.stackoverflow.StackOverflowQuestionResponse;
import ru.tinkoff.edu.scrapper.entity.Link;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
// FIXME: refactor, introduce link update handler (GithubUpdateHandler, SOUpdateHandler)
public class LinksUpdaterImpl implements LinksUpdater {
    private final GithubClient githubClient;

    private final StackOverflowClient stackOverflowClient;

    private final LinkParserService linkParserService;

    private record LinkAndParsingResult<T extends ParsingResult>(Link link, T parsed) {
    }

    @Override
    public List<LinkUpdateDelta> updateLinks(List<Link> links) {
        List<LinkAndParsingResult<GithubRepository>> githubLinks = new ArrayList<>();
        List<LinkAndParsingResult<StackOverflowQuestion>> stackOverflowLinks = new ArrayList<>();

        for (Link link : links) {
            ParsingResult result = linkParserService.parse(link.getUrl());

            link.setLastCheckedAt(OffsetDateTime.now());

            if (result == null) {
                throw new RuntimeException(
                        "Expected a parsable link, found " + link.getUrl()); // TODO: create exception hierarchy
            }

            if (result instanceof GithubRepository githubRepository) {
                githubLinks.add(new LinkAndParsingResult<>(link, githubRepository));
            } else if (result instanceof StackOverflowQuestion stackOverflowQuestion) {
                stackOverflowLinks.add(new LinkAndParsingResult<>(link, stackOverflowQuestion));
            } else {
                throw new RuntimeException(
                        "Expected " + link.getUrl() + " to be github or stackoverflow link, found " + result);
            }
        }

        List<LinkUpdateDelta> updatedLinks = new ArrayList<>();

        handleGithubLinks(updatedLinks, githubLinks);

        handleStackOverflowLinks(updatedLinks, stackOverflowLinks);

        return updatedLinks;
    }

    private void handleGithubLinks(List<LinkUpdateDelta> updatedLinks, List<LinkAndParsingResult<GithubRepository>> links) {
        for (var linkAndResult : links) {
            Link link = linkAndResult.link();
            GithubRepository parsingResult = linkAndResult.parsed();

            githubClient.checkRepository(parsingResult)
                    .flatMap(response -> handleGithubLink(link, response))
                    .ifPresent(updatedLinks::add);
        }
    }

    private static Optional<LinkUpdateDelta> handleGithubLink(Link link, GithubRepositoryResponse response) {
        return handleLinkUpdate(link,
                response.updatedAt(),
                response.openIssuesCount(),
                "got a new issue!");
    }

    private void handleStackOverflowLinks(List<LinkUpdateDelta> updatedLinks,
                                          List<LinkAndParsingResult<StackOverflowQuestion>> links
    ) {
        if (links.isEmpty()) {
            return;
        }

        // order in response is not the one that we need, so map id to link,
        // so we can change link fast
        Map<Long, Link> idToLink = links
                .stream()
                .collect(Collectors.toMap(
                        (r) -> Long.parseLong(r.parsed().id()),
                        LinkAndParsingResult::link
                ));

        List<StackOverflowQuestion> results = links.stream().map(LinkAndParsingResult::parsed).toList();

        stackOverflowClient.checkQuestions(results).ifPresent((response) -> {
            for (StackOverflowQuestionResponse item : response.items()) {
                handleStackOverflowLink(idToLink, item).ifPresent(updatedLinks::add);
            }
        });
    }

    private static Optional<LinkUpdateDelta> handleStackOverflowLink(
            Map<Long, Link> idToLink,
            StackOverflowQuestionResponse item
    ) {
        return handleLinkUpdate(
                idToLink.get(item.id()),
                item.updatedAt(),
                item.answerCount(),
                "got a new answer!"
        );
    }

    private static Optional<LinkUpdateDelta> handleLinkUpdate(
            Link link,
            OffsetDateTime updatedAt,
            Integer updatesCount,
            String updatesCountChangeMessage
    ) {
        boolean sameUpdatesCount = Objects.equals(link.getUpdatesCount(), updatesCount);
        boolean sameUpdatedAt = link.wasUpdatedAt(updatedAt);

        // first check, do not alert
        if (link.getUpdatedAt() == null) {
            link.setUpdatedAt(updatedAt);
            link.setUpdatesCount(updatesCount);
        } else if (!sameUpdatedAt || !sameUpdatesCount) {
            link.setUpdatedAt(updatedAt);
            link.setUpdatesCount(updatesCount);

            String message = sameUpdatesCount ? "something happened!" : updatesCountChangeMessage;

            return Optional.of(new LinkUpdateDelta(link, message));
        }

        return Optional.empty();
    }
}
