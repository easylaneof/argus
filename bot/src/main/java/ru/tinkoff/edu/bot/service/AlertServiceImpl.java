package ru.tinkoff.edu.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.dto.LinkUpdate;
import ru.tinkoff.edu.bot.processor.message.MessageSender;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AlertServiceImpl implements AlertService {
    private final TelegramBot bot;
    private final MessageSender messageSender;

    @Override
    public void alertAboutUpdate(LinkUpdate linkUpdate) {
        Map<String, ?> model = Map.of("description", linkUpdate.description(), "url", linkUpdate.url());

        for (long chatId : linkUpdate.tgChatIds()) {
            SendMessage message = messageSender.sendTemplate(chatId, "alert.ftlh", model);
            bot.execute(message);
        }
    }
}
