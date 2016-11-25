package client;

import com.Message;
import com.Messages;
import com.Packet;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jordan on 11/14/2016.
 */
public class ClientApplication {

    public static void main(String[] args) {

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
            try {
                final DatagramSocket socket = new DatagramSocket();

                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                byte[] recData = new byte[1024];
                                DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
                                socket.receive(recPacket);
                                final Packet[] packets1 = new Packet[1];
                                packets1[0] = Messages.decodeToPacket(recData);
                                System.out.println(Messages.decodeToMessage(packets1).toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();

                while (true) {
                    final Scanner scanner = new Scanner(System.in);
                    System.out.println("Please enter your message.  To retrieve messages, type \'GET\'.");
                    final String msg = scanner.nextLine();

                    InetAddress address = InetAddress.getByName(commandLine.getOptionValue("server-ip"));

                    if (!msg.equalsIgnoreCase("GET")) {
                        System.out.println("SENDING MSG");
                        final List<Packet> packets = new Message(Message.Type.SEND, "0000000002", "0000000001", msg).toPackets();
                        for (Packet packet : packets) {
                            final byte[] bytes = packet.encode();
                            final DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, 6969);
                            socket.send(datagramPacket);
                        }
                        System.out.println("SENT MSG");
                    } else {
                        System.out.println("SENDING [GET]");
                        final List<Packet> packets = new Message(Message.Type.GET, "0000000002", null, null).toPackets();
                        for (Packet packet : packets) {
                            final byte[] bytes = packet.encode();
                            final DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, 6969);
                            socket.send(datagramPacket);
                        }
                        System.out.println("SENT [GET]");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
