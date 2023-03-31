package ru.tinkoff.edu.bot.processor;


import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.processor.message.MessageSender;
import ru.tinkoff.edu.bot.processor.util.CommandParser;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.service.LinkService;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrackCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private final CommandParser commandParser;

    private final MessageSender messageSender;

    private final LinkService linkService;

    @Override
    public BotCommand command() {
        return new BotCommand("track", "Add a new link to track: /track <link>");
    }

    @Override
    public SendMessage process(Update update) {
        Optional<String> maybeLink = commandParser.parseCommandArgument("track", update.message().text());

        if (maybeLink.isEmpty()) {
            logFailedLink(update);

            return messageSender.send(update, "Failed parsing link from your message");
        }

        String link = maybeLink.get();

        Optional<LinkResponse> result = linkService.trackLink(update.message().chat().id(), link);

        if (result.isPresent()) {
            log.info("User with id {} added link {}", update.message().from().id(), link);

            return messageSender.send(update, "Added link %s".formatted(link));
        } else {
            logFailedLink(update);

            return messageSender.send(update, "Failed adding link %s".formatted(link));
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
