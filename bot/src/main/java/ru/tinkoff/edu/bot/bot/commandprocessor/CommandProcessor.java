package ru.tinkoff.edu.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import ru.tinkoff.edu.bot.bot.Command;


public interface CommandProcessor<T extends BaseRequest<T, R>, R extends BaseResponse> {
    Command command();

    /**
     * Precondition: canProcess(update) == true
     */
    BaseRequest<T, R> process(Update update);

    default boolean canProcess(Update update) {
        return update.message() != null &&
               update.message().text() != null &&
               update.message().text().startsWith("/" + command().name());
    }
}
