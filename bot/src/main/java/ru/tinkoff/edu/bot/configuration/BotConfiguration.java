package ru.tinkoff.edu.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.bot.bot.BotUpdatesDispatcher;
import ru.tinkoff.edu.bot.bot.commandprocessor.CommandProcessorFacade;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationProperties applicationProperties;

    @Bean
    TelegramBot telegramBot(CommandProcessorFacade commandProcessorFacade) {
        TelegramBot bot = new TelegramBot(applicationProperties.bot().apiKey());

        var botUpdatesDispatcher = new BotUpdatesDispatcher(bot, commandProcessorFacade);
        bot.setUpdatesListener(botUpdatesDispatcher);

        bot.execute(new SetMyCommands(botUpdatesDispatcher.getCommands().toArray(new BotCommand[0])));

        return bot;
    }
}
