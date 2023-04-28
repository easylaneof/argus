package ru.tinkoff.edu.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.dto.LinkResponse;
import ru.tinkoff.edu.bot.processor.message.MessageSender;
import ru.tinkoff.edu.bot.service.LinkService;

@Component
@RequiredArgsConstructor
public class ListCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private final LinkService linkService;

    private final MessageSender messageSender;

    @Override
    public BotCommand command() {
        return Command.LIST.toApiCommand();
    }

    @Override
    public SendMessage process(Update update) {
        List<LinkResponse> links = linkService.getAllLinks(update.message().chat().id());

        return messageSender.sendTemplate(update, "list.ftlh", Map.of("links", links));
    }
}
