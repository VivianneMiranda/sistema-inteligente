package devices;

import java.net.*;
import java.io.*;

public class TemperatureSensor {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Uso: java devices.SensorTemperatura <id> <type> <temperature>");
            return;
        }

        String id = args[0];
        String type = args[1];
        String temperature = args[2];

        try (MulticastSocket socket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName("230.0.0.1");
            int port = 6001;

            while (true) {
                String message = "DEVICE_REGISTER:" + id + ":" + type + ":" + temperature;
                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, port);
                socket.send(packet);

                System.out.println("Temperatura enviada: " + message);
                Thread.sleep(30000); // Envia a cada 30 segundos
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro no sensor de temperatura: " + e.getMessage());
        }
    }
}
