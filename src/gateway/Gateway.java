package gateway;

import java.io.*;
import java.net.*;
import java.util.*;

public class Gateway {
    private static final int TCP_PORT = 6000;
    private static final int MULTICAST_PORT = 6001;
    private static final String MULTICAST_GROUP = "230.0.0.1";

    private static Map<String, String> devices = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Gateway iniciado...");

        // Inicia listener para multicast
        new Thread(() -> startMulticastListener()).start();

        // Inicia servidor TCP
        startTCPServer();
    }

    private static void startMulticastListener() {
        try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
            socket.joinGroup(InetAddress.getByName(MULTICAST_GROUP));
            System.out.println("Escutando mensagens multicast...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                processMulticast(message, packet.getAddress());
            }
        } catch (IOException e) {
            System.err.println("Erro no multicast: " + e.getMessage());
        }
    }

    private static void processMulticast(String message, InetAddress address) {
        if (message.startsWith("DEVICE_REGISTER")) {
            String[] parts = message.split(":");
            String id = parts[1];
            String type = parts[2];
            String state = parts[3];
            devices.put(id, type + " - Estado: " + state);
            System.out.println("Dispositivo registrado: " + id + " (" + type + ")");
        }
    }

    private static void startTCPServer() {
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("Servidor TCP iniciado na porta " + TCP_PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String command;
                    while ((command = in.readLine()) != null) {
                        processCommand(command, out);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor TCP: " + e.getMessage());
        }
    }

    private static void processCommand(String command, PrintWriter out) {
        if (command.equals("TEST_CONNECTION")) {
            out.println("Conexão bem-sucedida!");
        } else if (command.equals("LIST_DEVICES")) {
            if (devices.isEmpty()) {
                out.println("Nenhum dispositivo conectado.");
            } else {
                for (Map.Entry<String, String> entry : devices.entrySet()) {
                    out.println(entry.getKey() + ": " + entry.getValue());
                }
                out.println("END");
            }
        } else if (command.startsWith("COMMAND")) {
            String[] parts = command.split(" ");
            String id = parts[1];
            String action = parts[2];

            if (devices.containsKey(id)) {
                devices.put(id, devices.get(id).split(" - ")[0] + " - Estado: " + action);
                out.println("Comando recebido: " + command);
            } else {
                out.println("Erro: Dispositivo não encontrado.");
            }
        } else {
            out.println("Comando não reconhecido.");
        }
    }
}
