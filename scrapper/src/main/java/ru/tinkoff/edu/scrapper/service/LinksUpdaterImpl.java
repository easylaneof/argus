package ru.tinkoff.edu.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.parser.LinkParserService;
import ru.tinkoff.edu.parser.ParsingResult;
import ru.tinkoff.edu.scrapper.client.github.GithubClient;
import ru.tinkoff.edu.scrapper.client.stackoverflow.StackOverflowClient;
import ru.tinkoff.edu.scrapper.client.stackoverflow.StackOverflowQuestionResponse;
import ru.tinkoff.edu.scrapper.entity.Link;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LinksUpdaterImpl implements LinksUpdater {
    private final GithubClient githubClient;

    private final StackOverflowClient stackOverflowClient;

    private final LinkParserService linkParserService;

    private record LinkAndParsingResult<T extends ParsingResult>(Link link, T parsed) {
    }

    @Override
    public List<Link> updateLinks(List<Link> links) {
        List<LinkAndParsingResult<ParsingResult.GithubRepository>> githubLinks = new ArrayList<>();
        List<LinkAndParsingResult<ParsingResult.StackOverflowQuestion>> stackOverflowLinks = new ArrayList<>();

        for (Link link : links) {
            ParsingResult result = linkParserService.parse(link.getUrl());

            link.setLastCheckedAt(OffsetDateTime.now());

            if (result == null) {
                throw new RuntimeException("Expected a parsable link, found " + link.getUrl()); // TODO: create exception hierarchy
            }

            if (result instanceof ParsingResult.GithubRepository githubRepository) {
                githubLinks.add(new LinkAndParsingResult<>(link, githubRepository));
            } else if (result instanceof ParsingResult.StackOverflowQuestion stackOverflowQuestion) {
                stackOverflowLinks.add(new LinkAndParsingResult<>(link, stackOverflowQuestion));
            } else {
                throw new RuntimeException("Expected " + link.getUrl() + " to be github or stackoverflow link, found " + result);
            }
        }

        List<Link> updatedLinks = new ArrayList<>();

        handleGithubLinks(updatedLinks, githubLinks);
        handleStackOverflow(updatedLinks, stackOverflowLinks);

        return updatedLinks;
    }

    private void handleGithubLinks(List<Link> updatedLinks, List<LinkAndParsingResult<ParsingResult.GithubRepository>> links) {
        for (var linkAndResult : links) {
            Link link = linkAndResult.link();
            ParsingResult.GithubRepository parsingResult = linkAndResult.parsed();

            githubClient.checkRepository(parsingResult).ifPresent((response) -> {
                OffsetDateTime updatedAt = response.updatedAt();

                // first check, do not alert
                if (link.getUpdatedAt() == null) {
                    link.setUpdatedAt(response.updatedAt());
                } else if (!Objects.equals(link.getUpdatedAt(), updatedAt)) {
                    link.setUpdatedAt(response.updatedAt());
                    updatedLinks.add(link);
                }
            });
        }
    }

    private void handleStackOverflow(List<Link> updatedLinks, List<LinkAndParsingResult<ParsingResult.StackOverflowQuestion>> links) {
        // order in response is not the one that we need, so map id to link,
        // so we can change link fast
        Map<Long, Link> idToLink = links
                .stream()
                .map(LinkAndParsingResult::link)
                .collect(Collectors.toMap(
                        Link::getId,
                        Function.identity()
                ));

        List<ParsingResult.StackOverflowQuestion> results = links.stream().map(LinkAndParsingResult::parsed).toList();

        stackOverflowClient.checkQuestions(results).ifPresent((response) -> {
            for (StackOverflowQuestionResponse item : response.items()) {
                Link link = idToLink.get(item.id());
                OffsetDateTime updatedAt = item.updatedAt();

                if (!Objects.equals(link.getUpdatedAt(), updatedAt)) {
                    link.setUpdatedAt(updatedAt);

                    updatedLinks.add(link);
                }
            }
        });
    }
}
