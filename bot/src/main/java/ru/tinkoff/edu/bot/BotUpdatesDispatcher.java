package ru.tinkoff.edu.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.bot.processor.CommandProcessorFacade;

@Slf4j
@RequiredArgsConstructor
public class BotUpdatesDispatcher implements UpdatesListener {
    static final String HELP_MESSAGE = "Unexpected message. Try using /help";

    private final TelegramBot bot;

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
                log.error("Exception while processing message", ex);

                return lastProcessedId;
            }
        }

        return CONFIRMED_UPDATES_ALL;
    }

    private SendMessage unexpectedMessage(Update update) {
        return new SendMessage(update.message().chat().id(), HELP_MESSAGE);
    }

    public List<BotCommand> getCommands() {
        return commandProcessorFacade.getCommands();
    }
}
