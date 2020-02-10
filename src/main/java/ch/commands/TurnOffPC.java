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
    private String SHUTDOWN_CMD = "net rpc shutdown -I %s -U %s";
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
        System.out.println(SHUTDOWN_CMD);
        shutdown();
    }

    private void shutdown() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", SHUTDOWN_CMD);

        try {
            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setProperties(Properties props) {
        ipAddr = props.getProperty("ip_addr");
        user = props.getProperty("user");
        password = props.getProperty("password");
    }
}
