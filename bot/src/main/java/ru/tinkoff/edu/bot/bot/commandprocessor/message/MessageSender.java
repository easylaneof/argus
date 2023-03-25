package ru.tinkoff.edu.bot.bot.commandprocessor.message;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Map;

public interface MessageSender {
    SendMessage send(Update update, String text);

    SendMessage sendTemplate(Update update, String templateName, Map<String, Object> data);

    default SendMessage sendTemplate(Update update, String templateName) {
        return sendTemplate(update, templateName, Map.of());
    }
}
