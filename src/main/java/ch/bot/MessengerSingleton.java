package ch.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessengerSingleton {
    private static MessengerSingleton instance = new MessengerSingleton();
    private long chatId;
    private MessengerObserver observer;

    private MessengerSingleton() {
        chatId = 0;
    }


    public static MessengerSingleton getInstance() {
        return instance;
    }

    public void sendMessageToUser(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        notifyObserver(sendMessage);
    }


    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void attach(MessengerObserver messengerObserver) {
        this.observer = messengerObserver;
    }

    private void notifyObserver(SendMessage sendMessage) {
        observer.update(sendMessage);
    }

}
