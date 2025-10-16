//UDP Server

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPChatServer {

    public static void main(String[] args) {
        // Define the port number the server will listen on
        int port = 9902;
        System.out.println("UDP Chat Server is starting...");

        // Use try-with-resources to ensure the socket is always closed
        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // The server runs indefinitely
            while (true) {
                // --- 1. Wait to receive a message from a client ---
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket); // This call blocks until a packet arrives

                // --- 2. Extract client's info and message ---
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                // **Important:** Convert only the received part of the buffer to a String
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("\n[Client at " + clientAddress.getHostAddress() + "]: " + clientMessage);
                
                // --- 3. Check for client exit message ---
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client has ended the session.");
                    continue; // Skip reply and wait for a new client message
                }

                // --- 4. Get server's reply from the console ---
                Scanner scanner = new Scanner(System.in);
                System.out.print("Your reply: ");
                String serverMessage = scanner.nextLine();

                // --- 5. Send the reply back to the client ---
                byte[] sendBuffer = serverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


//UDP CLient

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPChatClient {

    public static void main(String[] args) {
        // Define server address and port
        String serverHostname = "localhost";
        int serverPort = 9902;
        System.out.println("UDP Chat Client is starting...");

        // Use try-with-resources for automatic closing of the socket and scanner
        try (DatagramSocket clientSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            // Get the server's IP address
            InetAddress serverAddress = InetAddress.getByName(serverHostname);

            while (true) {
                // --- 1. Get a message from the user ---
                System.out.print("Your message (type 'exit' to quit): ");
                String clientMessage = scanner.nextLine();

                // --- 2. Send the message to the server ---
                byte[] sendBuffer = clientMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
                clientSocket.send(sendPacket);
                
                // --- 3. Check for exit condition ---
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("You have ended the chat session.");
                    break;
                }

                // --- 4. Wait to receive the server's reply ---
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(receivePacket); // Blocks until a reply is received

                // --- 5. Display the server's reply ---
                String serverReply = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("[Server]: " + serverReply);
            }

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Client has shut down.");
    }
}
