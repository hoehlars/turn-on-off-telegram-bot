package ch.commands;

import ch.bot.MessengerSingleton;
import ch.helper.PropertiesReader;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class TurnOnPC implements Command {
    private String broadcastIp;
    private String macAddrPC;
    public static final int PORT = 9;
    public static final String PC_ADRESSES = "turnOn.properties";
    private MessengerSingleton messengerSingleton;


    public TurnOnPC() {
        Properties props = PropertiesReader.readProperties(PC_ADRESSES);
        setProperties(props);
        messengerSingleton = MessengerSingleton.getInstance();
    }

    @Override
    public void execute(Update update) {
        sendMagicPacket();
        messengerSingleton.sendMessageToUser("Turned PC on!");
    }

    private void sendMagicPacket() {
        try {
            byte[] macBytes = getMacBytes(macAddrPC);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            InetAddress address = InetAddress.getByName(broadcastIp);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
        }
        catch (Exception e) {
            System.out.println("Failed to send Wake-on-LAN packet: + e");
        }
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

    private void setProperties(Properties props) {
        broadcastIp = props.getProperty("broadcast_ip");
        macAddrPC = props.getProperty("mac_addr");
    }
}
