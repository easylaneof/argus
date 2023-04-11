package ru.tinkoff.edu.bot.service;

import ru.tinkoff.edu.bot.dto.LinkUpdate;

public interface AlertService {
    void alertAboutUpdate(LinkUpdate linkUpdate);
}
