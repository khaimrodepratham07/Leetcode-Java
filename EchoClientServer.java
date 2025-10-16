import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {

    public static void main(String[] args) {
        // Server hostname and port number.
        String hostname = "localhost"; // Use "localhost" or "127.0.0.1" to connect to a server on the same machine.
        int portNumber = 9000;

        // Use try-with-resources to manage all sockets and streams.
        try (
            // Create a socket to connect to the server.
            Socket echoSocket = new Socket(hostname, portNumber);
            // Writer to send data to the server.
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            // Reader to get data from the server.
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            // Reader for user input from the console.
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to the echo server. Type your message and press Enter.");
            System.out.println("Type 'exit' to quit.");
            
            String userInput;
            // Loop to read user input from the console.
            while ((userInput = stdIn.readLine()) != null) {
                // Send the user's message to the server.
                out.println(userInput);
                
                // If the user types 'exit', break the loop and close the client.
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                // Wait for and print the server's echoed response.
                System.out.println("Server echo: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostname);
            System.exit(1);
        }
        
        System.out.println("Client shutting down.");
    }
}



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) {
        // The port number the server will listen on.
        int portNumber = 9000;
        System.out.println("Echo Server started. Listening on port " + portNumber);

        // Use try-with-resources to automatically close the ServerSocket.
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            
            // The server runs indefinitely, waiting for client connections.
            while (true) {
                // Wait for a client to connect. This is a blocking call.
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Use try-with-resources to manage client-specific resources.
                try (
                    // Writer to send data back to the client.
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                    // Reader to get data from the client.
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    String inputLine;
                    // Read from the client until the client closes the connection (readLine() returns null).
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Received from client: " + inputLine);
                        // Echo the received message back to the client.
                        out.println(inputLine);
                    }
                } catch (IOException e) {
                    System.out.println("Exception handling client " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
                } finally {
                     // When the inner try-with-resources block finishes, the client socket is closed.
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port " + portNumber);
            System.out.println(e.getMessage());
        }
    }
}
