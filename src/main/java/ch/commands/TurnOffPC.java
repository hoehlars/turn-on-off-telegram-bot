package ch.commands;

import ch.bot.MessengerSingleton;
import ch.helper.PropertiesReader;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class TurnOffPC implements Command {
    private String ipAddr;
    private String user;
    private String password;
    private String SHUTDOWN_CMD = "sudo net rpc shutdown -I %s -U %s";
    public static final String PC_INFORMATION = "turnOff.properties";
    private MessengerSingleton messengerSingleton;

    public TurnOffPC() {
        Properties props = PropertiesReader.readProperties(PC_INFORMATION);
        setProperties(props);
        messengerSingleton = MessengerSingleton.getInstance();
    }

    @Override
    public void execute(Update update) {
        String userAndPassword = user + "%" + password;
        SHUTDOWN_CMD = String.format(SHUTDOWN_CMD, ipAddr, userAndPassword);
        messengerSingleton.sendMessageToUser("Turned PC off!");
        shutdown();
    }

    private void shutdown() {
        Process p;
        try {
            p = Runtime.getRuntime().exec(SHUTDOWN_CMD);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                System.out.println("[LINE]: " + tmp);
            }
            p.waitFor();
            System.out.println("[EXIT]: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {

        }
    }

    private void setProperties(Properties props) {
        ipAddr = props.getProperty("ip_addr");
        user = props.getProperty("user");
        password = props.getProperty("password");
    }
}
