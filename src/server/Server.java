package server;

import com.Message;
import com.Messages;
import com.Packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jordan Florchinger on 11/15/2016.
 */
public class Server {

    private final DatagramSocket socket;
    private final HashMap<String, List<Packet>> packetHashMap;

    public Server(final int port) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.packetHashMap = new HashMap<>();
    }

    public void start() {
        while (true) {
            try {
                byte[] receiveData = new byte[1024];
                final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                final Packet packet = Messages.decodeToPacket(receiveData);

                if (packet.getType() == Message.Type.GET) {
                    System.out.println("RECEIVED [GET]");
                    final List<Packet> packets = packetHashMap.remove(packet.getSourceId());
                    InetAddress address = receivePacket.getAddress();
                    int port = receivePacket.getPort();
                    if (packets != null) {
                        for (Packet p : packets) {
                            byte[] sendData = p.encode();
                            final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                            socket.send(sendPacket);
                        }
                    } else {
                        System.out.println("Packet NULL");
                        final Packet p = new Packet(0, Message.Type.SEND, null, packet.getSourceId(), "Error: No Messages, forever alone loser.");
                        final byte[] bytes = p.encode();
                        final DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, address, port);
                        socket.send(sendPacket);
                    }
                    System.out.println("RESPONDED [GET]");
                } else {
                    System.out.println("RECEIVED REG -- STORING");
                    if (!packetHashMap.containsKey(packet.getDestinationId())) {
                        packetHashMap.put(packet.getDestinationId(), new ArrayList<>());
                    }
                    packetHashMap.get(packet.getDestinationId()).add(packet);
                    System.out.println("STORED");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
