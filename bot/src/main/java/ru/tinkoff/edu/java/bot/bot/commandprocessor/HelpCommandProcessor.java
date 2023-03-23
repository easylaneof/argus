package ru.tinkoff.edu.java.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.Command;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HelpCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private static final String HELP_ITEM_FORMAT = "/%s: %s";

    @Setter(AccessLevel.PACKAGE)// recursive dep, so we have to use setter
    private List<Command> commands;

    @Override
    public Command command() {
        return new Command("help", "Show this help");
    }

    @Override
    public SendMessage process(Update update) {
        return new SendMessage(update.message().chat().id(), formatCommands());
    }

    private String formatCommands() {
        return "List of all commands:\n\n" + commands
                .stream()
                .map(command -> HELP_ITEM_FORMAT.formatted(command.name(), command.description()))
                .collect(Collectors.joining("\n"));
    }
}
