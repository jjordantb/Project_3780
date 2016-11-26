package server;

import com.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jordan Florchinger on 11/15/2016.
 */
public class Server {

    private final DatagramSocket socket;
    private final Map<String, List<Packet>> packetHashMap;
    private final Map<String, InetAddress> usersMap;

    private boolean running = true;

    Server(final int port) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.packetHashMap = Collections.synchronizedMap(new HashMap<String, List<Packet>>());
        this.usersMap = Collections.synchronizedMap(new HashMap<String, InetAddress>());
    }

    void start() {
        while (running) {
            try {
                byte[] receiveData = new byte[1024];
                final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                new Responder(this.socket, receivePacket, this.usersMap, this.packetHashMap);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    public Map<String, List<Packet>> getPacketHashMap() {
        return packetHashMap;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
