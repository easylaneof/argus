package ru.tinkoff.edu.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.bot.BotUpdatesDispatcher;
import ru.tinkoff.edu.bot.processor.CommandProcessorFacade;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationProperties applicationProperties;

    @Bean
    TelegramBot telegramBot(CommandProcessorFacade commandProcessorFacade, MeterRegistry meterRegistry) {
        TelegramBot bot = new TelegramBot(applicationProperties.bot().apiKey());

        var botUpdatesDispatcher = new BotUpdatesDispatcher(bot, commandProcessorFacade, meterRegistry);
        bot.setUpdatesListener(botUpdatesDispatcher);

        bot.execute(new SetMyCommands(botUpdatesDispatcher.getCommands().toArray(new BotCommand[0])));

        return bot;
    }
}
