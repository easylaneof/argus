package ru.tinkoff.edu.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.processor.message.MessageSender;
import ru.tinkoff.edu.bot.service.ChatService;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartCommandProcessor implements CommandProcessor<SendMessage, SendResponse> {
    private final MessageSender messageSender;
    private final ChatService chatService;

    @Override
    public BotCommand command() {
        return Command.START.toApiCommand();
    }

    @Override
    public SendMessage process(Update update) {
        log.info("New user with id {} is added", update.message().from().id());

        chatService.registerChat(update.message().chat().id());

        return messageSender.sendTemplate(update, "start.ftlh");
    }
}
