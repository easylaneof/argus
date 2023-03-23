package ru.tinkoff.edu.java.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.Command;
import ru.tinkoff.edu.java.bot.bot.commandprocessor.util.CommandParser;
import ru.tinkoff.edu.java.bot.dto.Link;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.util.List;

@Component
public class ListCommandProcessor extends AbstractCommandProcessor<SendMessage, SendResponse> {
    public ListCommandProcessor(HelpCommandProcessor helpCommandProcessor, CommandParser commandParser, LinkService linkService) {
        super(helpCommandProcessor, commandParser, linkService);
    }

    @Override
    public Command command() {
        return new Command("list", "List of your links");
    }

    @Override
    public SendMessage process(Update update) {
        List<Link> links = linkService.getAllLinks(update.message().chat().id());

        String message = makeMessageByLinks(links);

        return send(update, message);
    }

    private String makeMessageByLinks(List<Link> links) {
        return null;
    }
}
