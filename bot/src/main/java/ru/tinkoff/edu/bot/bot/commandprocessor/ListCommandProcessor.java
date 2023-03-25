package ru.tinkoff.edu.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.bot.commandprocessor.message.MessageSender;
import ru.tinkoff.edu.bot.dto.Link;
import ru.tinkoff.edu.bot.service.LinkService;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ListCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private final LinkService linkService;

    private final MessageSender messageSender;

    @Override
    public BotCommand command() {
        return new BotCommand("list", "List of your links");
    }

    @Override
    public SendMessage process(Update update) {
        List<Link> links = linkService.getAllLinks(update.message().chat().id());

        return messageSender.sendTemplate(update, "list.ftlh", Map.of("links", links));
    }
}
