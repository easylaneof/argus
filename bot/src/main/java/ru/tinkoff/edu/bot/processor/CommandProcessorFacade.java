package ru.tinkoff.edu.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandProcessorFacade {
    private final List<CommandProcessor<?, ?>> commandProcessors;
    private final HelpCommandProcessor helpCommandProcessor;

    // FIXME?
    @PostConstruct
    private void init() {
        helpCommandProcessor.setCommands(getCommands());
    }

    public Optional<BaseRequest<?, ?>> process(Update update) {
        return findRequiredProcessor(update).map(p -> p.process(update));
    }

    private Optional<CommandProcessor<?, ?>> findRequiredProcessor(Update update) {
        return commandProcessors.stream().filter(p -> p.canProcess(update)).findAny();
    }

    public List<BotCommand> getCommands() {
        return commandProcessors
                .stream()
                .map(CommandProcessor::command)
                .toList();
    }
}
