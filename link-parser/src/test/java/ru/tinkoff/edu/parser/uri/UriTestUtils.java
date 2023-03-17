package ru.tinkoff.edu.parser.uri;

import java.net.URI;

public final class UriTestUtils {
    private UriTestUtils() {
    }

    public static final String GITHUB_URL_FORMAT = "https://github.com/%s/%s";

    private static final String STACK_OVERFLOW_URL_FORMAT = "https://stackoverflow.com/questions/%s/%s";

    public static URI githubURI(String user, String repository) {
        return URI.create(githubLink(user, repository));
    }

    public static URI githubURIWithTrailingSlash(String user, String repository) {
        return URI.create(githubLink(user, repository) + "/");
    }

    public static String githubLink(String user, String repository) {
        return GITHUB_URL_FORMAT.formatted(user, repository);
    }

    public static URI stackOverflowURI(String questionId) {
        return URI.create(stackOverflowLink(questionId));
    }

    public static URI stackOverflowUriWithTrailingSlash(String questionId) {
        return URI.create(stackOverflowLink(questionId) + "/");
    }

    public static String stackOverflowLink(String questionId) {
        return STACK_OVERFLOW_URL_FORMAT.formatted(questionId, questionId);
    }
}
