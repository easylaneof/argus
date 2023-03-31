package ru.tinkoff.edu.bot.processor.message;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@RequiredArgsConstructor
@Component
public final class MessageSenderImpl implements MessageSender {
    private final Configuration templateResolver;

    @Override
    public SendMessage send(Update update, String text) {
        return new SendMessage(update.message().chat().id(), text);
    }

    @SneakyThrows
    public SendMessage sendTemplate(Update update, String templateName, Map<String, Object> data) {
        Template template = templateResolver.getTemplate(templateName);
        Writer result = new StringWriter();
        template.process(data, result);
        return new SendMessage(update.message().chat().id(), result.toString())
                .parseMode(ParseMode.HTML);
    }
}
