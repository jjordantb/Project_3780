package client;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Scanner;

/**
 * Created by Jordan on 11/14/2016.
 */
public class ClientApplication {

    public static void main(String[] args) throws IOException {

        Options options = new Options();
        Option serverHost = new Option("s", "server-ip", true, "ip address of the server for this client");
        serverHost.setRequired(true);
        options.addOption(serverHost);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        if (commandLine != null) {
            System.out.println("Enter a Unique 10-Digit ID: ");
            final Scanner scanner = new Scanner(System.in);
            final String userId = scanner.nextLine();

            if (userId.length() == 10) {
                final DatagramSocket socket = new DatagramSocket();
                final Receiver receiver = new Receiver(socket);
                final Sender sender = new Sender(socket, commandLine.getOptionValue("server-ip"), userId);
            } else {
                System.out.println("Invalid UserID");
            }
        }
    }

}
