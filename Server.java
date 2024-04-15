import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(443)) {
            System.out.println("Bound to port: 443");
            System.out.println("Welcome to chat!");

            while (true) {
                Socket clientSocket = ss.accept();
                System.out.println("Connection from: " + clientSocket.getInetAddress());

                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String username = br.readLine();

                ClientHandler clientHandler = new ClientHandler(clientSocket, username);
                clients.add(clientHandler);
                clientHandler.start();

                broadcastMessage(username + " has joined the chat.", null);
            }
        } catch (IOException e) {
            System.out.println("Error in Server: " + e.getMessage());
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    static class ClientHandler extends Thread {
        private BufferedReader br;
        private PrintWriter pw;
        private String username;

        public ClientHandler(Socket clientSocket, String username) {
            try {
                this.username = username;
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                pw = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Error creating ClientHandler: " + e.getMessage());
            }
        }

        public void sendMessage(String message) {
            pw.println(message);
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = br.readLine()) != null) {
                    if ("switch".equals(message)) {
                        // Handle user switch
                        pw.println("You have switched users.");
                        continue;
                    }
                    broadcastMessage(username + ": " + message, this);
                }
            } catch (IOException e) {
                System.out.println("Error reading message: " + e.getMessage());
            } finally {
                try {
                    br.close();
                    pw.close();
                } catch (IOException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        }
    }
}
