package ru.tinkoff.edu.bot.bot.commandprocessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.bot.Command;
import ru.tinkoff.edu.bot.bot.commandprocessor.message.MessageSender;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private final MessageSender messageSender;

    @Override
    public Command command() {
        return new Command("start", "Registration of a new user");
    }

    @Override
    public SendMessage process(Update update) {
        log.info("New user with id {} is added", update.message().from().id());
        return messageSender.sendTemplate(update, "start.ftlh");
    }
}
