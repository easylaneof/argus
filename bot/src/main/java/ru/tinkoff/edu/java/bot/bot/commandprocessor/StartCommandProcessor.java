package ru.tinkoff.edu.java.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.Command;
import ru.tinkoff.edu.java.bot.bot.commandprocessor.util.CommandParser;
import ru.tinkoff.edu.java.bot.service.LinkService;

@Component
@Slf4j
public class StartCommandProcessor extends AbstractCommandProcessor<SendMessage, SendResponse> {
    public StartCommandProcessor(HelpCommandProcessor helpCommandProcessor, CommandParser commandParser, LinkService linkService) {
        super(helpCommandProcessor, commandParser, linkService);
    }

    @Override
    public Command command() {
        return new Command("start", "Registration of a new user");
    }

    @Override
    public SendMessage process(Update update) {
        log.info("New user with id {} is added", update.message().from().id());
        return send(update, "Welcome onboard!");
    }
}
