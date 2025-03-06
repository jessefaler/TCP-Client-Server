import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static String host = "";
    public static int port = -1;

    public static void main(String[] args) {
        processArguments(args);                       // Retrieve the host and port arguments
        TCPClient client = new TCPClient(host, port); // Create a TCP client
        client.connect();                             // Connect to the server
        // Prompt the user for a short and send it to the server
        for (;;) {
            short num = promptUser();
            long startTimeMillis = System.currentTimeMillis();
            client.sendShort(num);
            long endTimeMillis = System.currentTimeMillis();
            System.out.println("\u001B[90mRound-trip time: \u001B[0m" + (endTimeMillis - startTimeMillis) + "ms");
        }
    }

    public static short promptUser() {
        short number;
        while (true) {
            System.out.print("Enter a valid decimal number (-32768 to 32767) or press Enter to use 16735: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return 16735;
            }

            try {
                number = Short.parseShort(input);
                return number;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mError: Invalid input. Please enter a valid decimal number.\u001B[0m");
            }
        }
    }

    public static void processArguments(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar TCPClient.jar <hostname> <port>");
            return;
        }

        host = args[0];
        port = -1;

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number: " + args[1]);
            throw new NumberFormatException();
        }
    }
}
