import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {

    private final int port;       // The port the host is found on
    private final String address; // The IP or Domain of the host
    private Socket socket;        // The client socket

    /**
     * @param address The port the host is found on
     * @param port The IP or Domain of the host
     */
    public TCPClient(String address, int port) {
        this.port = port;
        this.address = address;
    }

    /**
     * Attempts to establish a connection to the endpoint.
     */
    public void connect() {
        try {
            socket = new Socket(address, port);
            if(socket.isConnected()) System.out.println("Established connection to: " + "\u001B[31m" + address + ":" + port + "\u001B[0m");
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Send a short to the endpoint.
     * @param value the short to send
     */
    public void sendShort(Short value) {
        byte[] bytes = new byte[2];              // Convert the short to a byte array
        bytes[0] = (byte) ((value >> 8) & 0xFF); // High byte
        bytes[1] = (byte) (value & 0xFF);        // Low byte
        sendBytes(bytes);                        // Send the short the the server
    }

    /**
     * Sends an array of bytes to the endpoint. <p>
     * After sending, it waits for and prints the server's response.
     * @param bytes the byte array to send
     */
    public void sendBytes(byte[] bytes) {
        try {
            System.out.println("\u001B[90mSending: \u001B[0m" + printBytesAsHex(bytes)); // Print the bytes being send to console
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            printResponse(); // Get the response from the server
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a response from the server, stopping when a newline ('\n') is encountered.
     * Prints the received message in hexadecimal format to the console.
     *
     * @throws IOException If an error occurs while reading the input stream.
     */
    public void printResponse() throws IOException {
        InputStream inputStream = socket.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        while ((b = inputStream.read()) != -1) {
            if (b == '\n') {
                break;
            }
            buffer.write(b);
        }
        if (buffer.size() == 0) {
            System.out.println("Connection was closed prematurely.");
            return;
        }
        byte[] responseBytes = buffer.toByteArray();
        System.out.println("\u001B[90mResponse: \u001B[0m" + printBytesAsHex(responseBytes));
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints a byte array to console in hexadecimal format
     * @param bytes the bytes to print to console
     */
    public static String printBytesAsHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte value : bytes) {
            hex.append(String.format("0x%02X ", value));
        }
        return hex.toString();
    }
}

