package ru.tinkoff.edu.bot.processor.message;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Map;

public interface MessageSender {
    SendMessage send(long chatId, String text);

    SendMessage sendTemplate(long chatId, String templateName, Map<String, ?> data);

    default SendMessage send(Update update, String text) {
        return send(update.message().chat().id(), text);
    }

    default SendMessage sendTemplate(Update update, String templateName, Map<String, ?> data) {
        return sendTemplate(update.message().chat().id(), templateName, data);
    }

    default SendMessage sendTemplate(Update update, String templateName) {
        return sendTemplate(update, templateName, Map.of());
    }

    default SendMessage sendTemplate(long chatId, String templateName) {
        return sendTemplate(chatId, templateName, Map.of());
    }
}
