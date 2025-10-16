//DNS SERVER

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class DnsServer {

    public static void main(String[] args) {
        // A map to act as our simple DNS database
        Map<String, String> dnsRecords = new HashMap<>();
        dnsRecords.put("yahoo.com", "68.180.206.184");
        dnsRecords.put("gmail.com", "209.85.148.19");
        dnsRecords.put("cricinfo.com", "80.168.92.140");
        dnsRecords.put("facebook.com", "69.63.189.16");

        int port = 1362;
        System.out.println("DNS Server is starting on port " + port);
        System.out.println("Press Ctrl+C to stop.");

        // Use try-with-resources to ensure the socket is always closed.
        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            // The server runs indefinitely, waiting for client requests.
            while (true) {
                // 1. Wait to receive a request packet from a client
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket); // Blocks until a packet is received

                // 2. Extract the hostname from the packet
                // Trim the string to remove any extra whitespace or null characters
                String hostnameQuery = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                System.out.println("Received request for: " + hostnameQuery);

                // 3. Look up the IP address in our DNS records
                // getOrDefault is a concise way to handle cases where the key is not found
                String ipAddressResponse = dnsRecords.getOrDefault(hostnameQuery, "Host Not Found");
                byte[] sendBuffer = ipAddressResponse.getBytes();

                // 4. Send the response packet back to the client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}

//DNS CLIENT

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DnsClient {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 1362;

        // Use try-with-resources for automatic closing of resources
        try (DatagramSocket clientSocket = new DatagramSocket();
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.print("Enter the hostname to look up (e.g., yahoo.com): ");
            String hostname = consoleReader.readLine();

            // 1. Send the hostname to the DNS server
            byte[] sendBuffer = hostname.getBytes();
            InetAddress ipAddress = InetAddress.getByName(serverAddress);
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, ipAddress, serverPort);
            clientSocket.send(sendPacket);

            // 2. Wait to receive the response from the server
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);

            // 3. Display the response
            String ipResponse = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            System.out.println("IP Address: " + ipResponse);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
