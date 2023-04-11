package ru.tinkoff.edu.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.scrapper.entity.Chat;
import ru.tinkoff.edu.scrapper.repository.ChatRepository;

@RequiredArgsConstructor
@Component
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(long chatId) {
        chatRepository.save(new Chat(chatId));
    }

    @Override
    public void unregister(long chatId) {
        chatRepository.deleteById(chatId);
    }
}
