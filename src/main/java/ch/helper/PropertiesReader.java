package ch.helper;

import ch.bot.WakeUpTurnOffBot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    public static Properties readProperties(String properties) {
        Properties prop = null;
        try (InputStream input = WakeUpTurnOffBot.class.getClassLoader().getResourceAsStream(properties)) {
            prop = new Properties();
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
