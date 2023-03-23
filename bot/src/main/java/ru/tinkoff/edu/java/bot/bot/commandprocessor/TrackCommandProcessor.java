package ru.tinkoff.edu.java.bot.bot.commandprocessor;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.Command;
import ru.tinkoff.edu.java.bot.bot.commandprocessor.util.CommandParser;
import ru.tinkoff.edu.java.bot.dto.Link;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.util.Optional;

@Component
@Slf4j
public class TrackCommandProcessor extends AbstractCommandProcessor<SendMessage, SendResponse> {
    public TrackCommandProcessor(HelpCommandProcessor helpCommandProcessor, CommandParser commandParser, LinkService linkService) {
        super(helpCommandProcessor, commandParser, linkService);
    }

    @Override
    public Command command() {
        return new Command("track", "Add a new link to track: /track <link>");
    }

    @Override
    public SendMessage process(Update update) {
        Optional<String> maybeLink = commandParser.parseCommandArgument("track", update.message().text());

        if (maybeLink.isEmpty()) {
            logFailedLink(update);

            return help(update);
        }

        String link = maybeLink.get();

        Optional<Link> result = linkService.trackLink(update.message().chat().id(), link);

        if (result.isPresent()) {
            log.info("User with id {} added link {}", update.message().from().id(), link);

            return send(update, "Added link %s".formatted(link));
        } else {
            logFailedLink(update);

            return send(update, "Failed adding link %s".formatted(link));
        }
    }

    private static void logFailedLink(Update update) {
        log.info(
                "User with id {} failed adding link with message {}",
                update.message().from().id(),
                update.message().text()
        );
    }
}
