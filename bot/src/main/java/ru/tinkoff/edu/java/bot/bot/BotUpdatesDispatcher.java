package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.commandprocessor.CommandProcessorFacade;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BotUpdatesDispatcher implements UpdatesListener {
    @Setter
    private TelegramBot bot;

    private final CommandProcessorFacade commandProcessorFacade;

    @Override
    public int process(List<Update> updates) {
        int lastProcessedId = CONFIRMED_UPDATES_NONE;

        for (Update update : updates) {
            if (update.message() == null) {
                continue;
            }

            try {
                bot.execute(commandProcessorFacade.process(update).orElse(unexpectedMessage(update)));

                lastProcessedId = update.updateId();
            } catch (RuntimeException ex) {
                return lastProcessedId;
            }
        }

        return CONFIRMED_UPDATES_ALL;
    }

    private SendMessage unexpectedMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Unexpected message. Try using /help");
    }
}
