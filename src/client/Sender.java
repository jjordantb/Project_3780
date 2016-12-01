package client;

import com.Message;
import server.ServerApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jordan on 11/26/2016.
 */
public class Sender implements Runnable {

    private static final Pattern SEND_PATTERN = Pattern.compile("SEND (.*?) TO (\\d{10})");

    private final DatagramSocket socket;
    private final Thread thread;

    private final String serverIp, userId;
    private boolean running = true;

    public Sender(final DatagramSocket socket, final String serverIp, final String userId) {
        this.socket = socket;
        this.serverIp = serverIp;
        this.userId = userId;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        while (this.running) {
            final Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter your message (SEND <message> TO <10-digit-UID>).  To retrieve messages, type \'GET\'.");
            final String msg = scanner.nextLine();

            InetAddress address = null;
            try {
                address = InetAddress.getByName(this.serverIp);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            final Matcher matcher = SEND_PATTERN.matcher(msg);
            if (matcher.find()) {
                this.sendPackets(address, new Message(0, Message.Type.SEND, this.userId, matcher.group(2), matcher.group(1))); // seq numb
            } else {
                if (msg.equalsIgnoreCase("GET")) {
                    this.sendPackets(address, new Message(0, Message.Type.GET, this.userId, null, null)); // seq numb
                }
            }
        }
    }

    private void sendPackets(final InetAddress address, final Message message) {
        final byte[] bytes = message.encode();
        final DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, ServerApplication.PORT);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
