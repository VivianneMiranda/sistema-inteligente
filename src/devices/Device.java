package devices;

import java.net.*;
import java.io.*;

public class Device {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Uso: java devices.Device <id> <type> <state>");
            return;
        }

        String id = args[0];
        String type = args[1];
        String state = args[2];

        try (MulticastSocket socket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName("230.0.0.1");
            int port = 6001;

            String message = "DEVICE_REGISTER:" + id + ":" + type + ":" + state;
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, port);
            socket.send(packet);

            System.out.println("Dispositivo iniciado: " + id + " (" + type + ") - Estado: " + state);
        } catch (IOException e) {
            System.err.println("Erro no dispositivo: " + e.getMessage());
        }
    }
}
