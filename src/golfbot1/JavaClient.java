package golfbot1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class JavaClient {
    public static void main(String[] args) {
        // Define the server host and port
        String host = "localhost";  // Change to the appropriate host if needed
        int port = 5000;  // Change to the appropriate port if needed

        // Retry connection until the server is available
        while (true) {
            try {
                // Attempt to create a socket object and connect to the server
                Socket socket = new Socket(host, port);
                System.out.println("Connected to server: " + host + ":" + port);

                // Create input and output streams for the socket
                final BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                // Create a thread to receive messages from the server
                Thread receiveThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                String response = input.readLine();
                                if (response == null) {
                                    break;
                                }
                                System.out.println("Received message from Python: " + response);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                receiveThread.start();

                // Send messages to the server
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = consoleInput.readLine()) != null) {
                    output.println(message);
                }

                // Close the connection
                socket.close();
            } catch (IOException e) {
                System.out.println("Connection failed. Retrying...");
                try {
                    // Wait for a while before retrying connection
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

