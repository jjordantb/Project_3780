package server;

import com.Message;
import com.Messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Jordan Florchinger on 11/15/2016.
 */
public class Server {

    private final DatagramSocket socket;

    private boolean running = true;

    public Server(final int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    public void start() throws IOException {
        byte[] recieveData = new byte[1024];
        while (this.running) {
            final DatagramPacket recievePacket = new DatagramPacket(recieveData, recieveData.length);
            this.socket.receive(recievePacket);
            final Message message = Messages.decode(recieveData);
            System.out.println("Server [RECEIVED]: " + message.toString());
        }
    }

}
