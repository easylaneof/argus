package ru.tinkoff.edu.scrapper.service.notifications;

import java.util.List;

public interface BotUpdateNotifier {
    void notifyBot(List<LinkUpdateDelta> updatedLinks);
}
