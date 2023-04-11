package ru.tinkoff.edu.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.bot.client.ScrapperClient;

@Component
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ScrapperClient scrapperClient;

    @Override
    public void registerChat(long chatId) {
        System.out.println(scrapperClient.registerChat(chatId));
    }
}
