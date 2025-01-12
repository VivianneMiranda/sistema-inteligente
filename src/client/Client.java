package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "44.202.168.187";
    private static final int SERVER_PORT = 6000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Cliente conectado ao Gateway!");

            while (true) {
                System.out.println("\nEscolha uma opção:");
                System.out.println("1. Testar conexão");
                System.out.println("2. Listar dispositivos");
                System.out.println("3. Enviar comando");
                System.out.println("4. Sair");
                System.out.print("Opção: ");
                String option = scanner.nextLine();

                switch (option) {
                    case "1":
                        out.println("TEST_CONNECTION");
                        String response = in.readLine();
                        System.out.println("Resposta do Gateway: " + (response != null ? response : "Erro ao testar conexão."));
                        break;

                    case "2":
                        out.println("LIST_DEVICES");
                        String dispositivos;
                        System.out.println("Dispositivos conectados:");
                        while ((dispositivos = in.readLine()) != null && !dispositivos.equals("END")) {
                            System.out.println(dispositivos);
                        }
                        break;

                    case "3":
                        System.out.print("ID do dispositivo: ");
                        String id = scanner.nextLine();
                        System.out.print("Ação (ligar/desligar): ");
                        String action = scanner.nextLine();
                        out.println("COMMAND " + id + " " + action);
                        System.out.println("Resposta do Gateway: " + in.readLine());
                        break;

                    case "4":
                        System.out.println("Encerrando cliente...");
                        return;

                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
