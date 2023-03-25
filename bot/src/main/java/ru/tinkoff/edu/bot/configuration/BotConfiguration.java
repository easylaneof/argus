package ru.tinkoff.edu.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.bot.bot.BotUpdatesDispatcher;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationConfig applicationConfig;
    private final BotUpdatesDispatcher botUpdatesDispatcher;

    @Bean
    TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(applicationConfig.bot().apiKey());

        bot.execute(new SetMyCommands(botUpdatesDispatcher.getCommands().toArray(new BotCommand[0])));

        botUpdatesDispatcher.setBot(bot); // FIXME circular dep :(
        bot.setUpdatesListener(botUpdatesDispatcher);

        return bot;
    }
}
