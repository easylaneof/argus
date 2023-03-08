package ru.tinkoff.edu.java.parser;

public sealed interface ParsingResult {
    record GithubRepository(String user, String name) implements ParsingResult {
    }

    record StackOverflowQuestion(String id) implements ParsingResult {
    }
}
