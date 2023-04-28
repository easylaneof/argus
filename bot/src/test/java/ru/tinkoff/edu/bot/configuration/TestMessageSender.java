package ru.tinkoff.edu.bot.configuration;

import freemarker.template.Configuration;
import java.io.File;
import ru.tinkoff.edu.bot.processor.message.MessageSender;
import ru.tinkoff.edu.bot.processor.message.MessageSenderImpl;
import static freemarker.template.Configuration.VERSION_2_3_31;

public class TestMessageSender {
    private static final Configuration templateResolver;

    static {
        try {
            templateResolver = new freemarker.template.Configuration(VERSION_2_3_31);

            File templatesDir = new File("src/main/resources/templates");

            templateResolver.setDirectoryForTemplateLoading(templatesDir);
            templateResolver.setDefaultEncoding("UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static MessageSender getMessageSender() {
        return new MessageSenderImpl(templateResolver);
    }
}
