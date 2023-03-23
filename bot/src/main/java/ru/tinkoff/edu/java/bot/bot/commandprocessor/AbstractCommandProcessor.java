package ru.tinkoff.edu.java.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.bot.commandprocessor.util.CommandParser;
import ru.tinkoff.edu.java.bot.service.LinkService;

@RequiredArgsConstructor
public abstract class AbstractCommandProcessor<T extends BaseRequest<T, R>, R extends BaseResponse> implements CommandProcessor<T, R> {
    private final HelpCommandProcessor helpCommandProcessor;

    protected final CommandParser commandParser;

    protected final LinkService linkService;

    protected SendMessage help(Update update) {
        return helpCommandProcessor.process(update);
    }

    protected SendMessage send(Update update, String text) {
        return new SendMessage(update.message().chat().id(), text);
    }
}
