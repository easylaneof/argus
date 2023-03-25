package ru.tinkoff.edu.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.bot.Command;
import ru.tinkoff.edu.bot.bot.commandprocessor.message.MessageSender;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class HelpCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private final MessageSender messageSender;

    @Setter(AccessLevel.PACKAGE)// recursive dep, so we have to use setter
    private List<Command> commands;

    @Override
    public Command command() {
        return new Command("help", "Show this help");
    }

    @Override
    public SendMessage process(Update update) {
        return messageSender.sendTemplate(update, "help.ftlh", Map.of("commands", commands));
    }
}
