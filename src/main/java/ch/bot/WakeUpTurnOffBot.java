package ch.bot;

import ch.commands.Command;
import ch.commands.TurnOffPC;
import ch.commands.TurnOnPC;
import ch.helper.AuthenticatorSingleton;
import ch.helper.PropertiesReader;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Properties;

public class WakeUpTurnOffBot extends org.telegram.telegrambots.bots.TelegramLongPollingBot implements MessengerObserver {
    private Command currentCommand;
    private String botToken;
    private String botName;
    private MessengerSingleton messengerSingleton;
    private AuthenticatorSingleton authenticator;

    public static final String BOT_PROPERTIES = "bot.properties";

    public WakeUpTurnOffBot() {
        Properties props = PropertiesReader.readProperties(BOT_PROPERTIES);
        setBotProperties(props);
        authenticator = AuthenticatorSingleton.getInstance();
        messengerSingleton = MessengerSingleton.getInstance();
        messengerSingleton.attach(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String user = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
        messengerSingleton.setChatId(update.getMessage().getChatId());

        if(authenticator.authenticate(user)) {
            String command = update.getMessage().getText();
            setCommandByText(command);
            if(currentCommand != null) {
                currentCommand.execute(update);
            }
        } else {
            messengerSingleton.sendMessageToUser("You're not allowed to turn off/on my pc!");
        }
    }

    private void setCommandByText(String commandString) {
        switch(commandString) {
            case Command.ON_CMD:
                currentCommand = new TurnOnPC();
                break;
            case Command.OFF_CMD:
                currentCommand = new TurnOffPC();
                break;
        }
    }

    private void setBotProperties(Properties props) {
        botToken = props.getProperty("token");
        botName = props.getProperty("name");
    }

    @Override
    public void update(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
