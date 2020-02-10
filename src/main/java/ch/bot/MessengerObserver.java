package ch.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessengerObserver {
    void update(SendMessage sendMessage);
}
