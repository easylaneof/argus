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
// FIXME: refactor
public class LinksUpdaterImpl implements LinksUpdater {
    private final GithubClient githubClient;

    private final StackOverflowClient stackOverflowClient;

    private final LinkParserService linkParserService;

    private record LinkAndParsingResult<T extends ParsingResult>(Link link, T parsed) {
    }

    @Override
    public List<Delta> updateLinks(List<Link> links) {
        List<LinkAndParsingResult<GithubRepository>> githubLinks = new ArrayList<>();
        List<LinkAndParsingResult<StackOverflowQuestion>> stackOverflowLinks = new ArrayList<>();

        for (Link link : links) {
            ParsingResult result = linkParserService.parse(link.getUrl());

            link.setLastCheckedAt(OffsetDateTime.now());

            if (result == null) {
                throw new RuntimeException("Expected a parsable link, found " + link.getUrl()); // TODO: create exception hierarchy
            }

            if (result instanceof GithubRepository githubRepository) {
                githubLinks.add(new LinkAndParsingResult<>(link, githubRepository));
            } else if (result instanceof StackOverflowQuestion stackOverflowQuestion) {
                stackOverflowLinks.add(new LinkAndParsingResult<>(link, stackOverflowQuestion));
            } else {
                throw new RuntimeException("Expected " + link.getUrl() + " to be github or stackoverflow link, found " + result);
            }
        }

        List<Delta> updatedLinks = new ArrayList<>();

        handleGithubLinks(updatedLinks, githubLinks);

        handleStackOverflowLinks(updatedLinks, stackOverflowLinks);

        return updatedLinks;
    }

    private void handleGithubLinks(List<Delta> updatedLinks, List<LinkAndParsingResult<GithubRepository>> links) {
        for (var linkAndResult : links) {
            Link link = linkAndResult.link();
            GithubRepository parsingResult = linkAndResult.parsed();

            githubClient.checkRepository(parsingResult)
                    .flatMap(response -> handleGithubLink(link, response))
                    .ifPresent(updatedLinks::add);
        }
    }

    private static Optional<Delta> handleGithubLink(Link link, GithubRepositoryResponse response) {
        OffsetDateTime updatedAt = response.updatedAt();
        Integer openIssuesCount = response.openIssuesCount();

        boolean sameUpdatesCount = Objects.equals(link.getUpdatesCount(), openIssuesCount);

        // first check, do not alert
        if (link.getUpdatedAt() == null) {
            link.setUpdatedAt(updatedAt);
            link.setUpdatesCount(openIssuesCount);
        } else if (!Objects.equals(link.getUpdatedAt(), updatedAt) || !sameUpdatesCount) {
            link.setUpdatedAt(updatedAt);
            link.setUpdatesCount(openIssuesCount);

            String message = sameUpdatesCount ? "something happened!" : "got a new issue!";

            return Optional.of(new Delta(link, message));
        }

        return Optional.empty();
    }

    private void handleStackOverflowLinks(List<Delta> updatedLinks, List<LinkAndParsingResult<ParsingResult.StackOverflowQuestion>> links) {
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

    private static Optional<Delta> handleStackOverflowLink(
            Map<Long, Link> idToLink,
            StackOverflowQuestionResponse item
    ) {
        Link link = idToLink.get(item.id());

        OffsetDateTime updatedAt = item.updatedAt();
        Integer answerCount = item.answerCount();
        boolean sameUpdatesCount = Objects.equals(link.getUpdatesCount(), answerCount);

        // first check, do not alert
        if (link.getUpdatedAt() == null) {
            link.setUpdatedAt(updatedAt);
            link.setUpdatesCount(answerCount);
        } else if (!Objects.equals(link.getUpdatedAt(), updatedAt) || !sameUpdatesCount) {
            link.setUpdatedAt(updatedAt);
            link.setUpdatesCount(answerCount);

            String message = sameUpdatesCount ? "something happened!" : "got a new answer!";

            return Optional.of(new Delta(link, message));
        }

        return Optional.empty();
    }
}
