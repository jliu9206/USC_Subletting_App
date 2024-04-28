package finalproject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private BufferedReader br;
    private PrintWriter pw;
    private Socket s;
    public String user;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try {
            String hostname = "localhost";
            int port = 443;
            s = new Socket(hostname, port);
            System.out.println("Connected to: " + hostname + ": " + port);
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your name:");
            user = userInputReader.readLine();

            pw = new PrintWriter(s.getOutputStream(), true); // Auto-flush enabled
            pw.println(user); // Send the username to the server immediately after connecting

            br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            // Start a thread to read messages from the server
            Thread readerThread = new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = br.readLine()) != null) {
                        System.out.println(receivedMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading message from server: " + e.getMessage());
                }
            });
            readerThread.start();

            // Start a thread to read user input and send messages to the server
            Thread userInputThread = new Thread(() -> {
                try {
                    BufferedReader userInputReaderThread = new BufferedReader(new InputStreamReader(System.in));
                    String userInput;
                    while ((userInput = userInputReaderThread.readLine()) != null) {
                        if ("quit".equals(userInput.toLowerCase())) { // Check for exit command
                        	System.out.println("Chat is ended.");
                            break; // Exit the loop and stop sending messages
                        }
                        pw.println(userInput); // Send user input to the server
                        pw.flush(); // Flush the PrintWriter to send the message immediately
                    }
                } catch (IOException e) {
                    System.err.println("Error reading user input: " + e.getMessage());
                }
            });
            userInputThread.start();

            // Join threads to keep the main thread alive
            readerThread.join();
            userInputThread.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (br != null)
                    br.close();
                if (pw != null)
                    pw.close();
                if (s != null)
                    s.close();
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
