package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.bot.BotUpdatesDispatcher;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationConfig applicationConfig;
    private final BotUpdatesDispatcher botUpdatesDispatcher;

    @Bean
    TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(applicationConfig.bot().apiKey());

        botUpdatesDispatcher.setBot(bot); // FIXME circular dep :(
        bot.setUpdatesListener(botUpdatesDispatcher);

        return bot;
    }
}
