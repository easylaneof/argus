package ru.tinkoff.edu.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;

public enum Command {
    START("start", "Register a new user"),
    HELP("help", "Show this help"),
    LIST("list", "List of your links"),
    TRACK("track", "Add a new link to track: /track <link>"),
    UNTRACK("untrack", "Untrack a link: /untrack <link>");

    private final String command;
    private final String description;

    private final BotCommand apiCommand;

    Command(String command, String description) {
        this.command = command;
        this.description = description;
        this.apiCommand = new BotCommand(command, description);
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public BotCommand toApiCommand() {
        return this.apiCommand;
    }
}
