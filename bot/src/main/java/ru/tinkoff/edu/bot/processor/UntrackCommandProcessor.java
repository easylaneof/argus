package ru.tinkoff.edu.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.processor.message.MessageSender;
import ru.tinkoff.edu.bot.processor.util.CommandParser;
import ru.tinkoff.edu.bot.service.LinkService;

@Component
@Slf4j
@RequiredArgsConstructor
public class UntrackCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private final CommandParser commandParser;
    private final MessageSender messageSender;
    private final LinkService linkService;

    @Override
    public BotCommand command() {
        return Command.UNTRACK.toApiCommand();
    }

    @Override
    public SendMessage process(Update update) {
        Optional<String> maybeLink = commandParser.parseCommandArgument("untrack", update.message().text());

        if (maybeLink.isEmpty()) {
            logFailedLink(update);

            return messageSender.send(update, "Failed parsing link from your message");
        }

        String link = maybeLink.get();

        Optional<LinkResponse> result = linkService.untrackLink(update.message().chat().id(), link);

        if (result.isPresent()) {
            log.info("User with id {} untracked link {}", update.message().from().id(), link);

            return messageSender.send(update, "Untracked link %s".formatted(link));
        } else {
            logFailedLink(update);

            return messageSender.send(update, "Failed untracking link %s".formatted(link));
        }
    }

    private void logFailedLink(Update update) {
        log.info(
            "User with id {} failed untracking link with message {}",
            update.message().from().id(),
            update.message().text()
        );
    }
}
