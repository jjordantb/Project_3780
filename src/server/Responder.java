package server;

import com.Message;
import com.Messages;
import com.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jordan on 11/24/2016.
 */
public class Responder implements Runnable {

    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private final Map<String, InetAddress> usersList;
    private final Map<String, List<Packet>> packetMap;

    private final Thread thread;

    public Responder(final DatagramSocket socket, final DatagramPacket packet,
                     final Map<String, InetAddress> usersList, final Map<String, List<Packet>> packetMap) {
        this.socket = socket;
        this.packet = packet;
        this.usersList = usersList;
        this.packetMap = packetMap;

        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        final byte[] receiveData = this.packet.getData();
        final Packet packet = Messages.decodeToPacket(receiveData);

        synchronized (this.packetMap) {
            if (packet.getType() == Message.Type.GET) {
                System.out.println("RECEIVED [GET]");
                final List<Packet> packets = this.packetMap.remove(packet.getSourceId());
                InetAddress address = this.packet.getAddress();
                int port = this.packet.getPort();
                if (packets != null) {
                    for (Packet p : packets) {
                        byte[] sendData = p.encode();
                        final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                        try {
                            socket.send(sendPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Packet NULL");
                    final Packet p = new Packet(0, Message.Type.SEND, null, packet.getSourceId(), "Error: No Messages, forever alone loser.");
                    final byte[] bytes = p.encode();
                    final DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, address, port);
                    try {
                        socket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("RESPONDED [GET]");
            } else if (packet.getType() == Message.Type.SEND) {
                if (packet.getPayload().equals("LOGIN")) { // send out to other servers
                    synchronized (this.usersList) {
                        if (!this.usersList.containsKey(packet.getSourceId())) {
                            this.usersList.put(packet.getSourceId(), this.packet.getAddress());
                        }
                    }
                } else if (packet.getPayload().equals("LOGOUT")) {
                    synchronized (this.usersList) {
                        if (this.usersList.containsKey(packet.getSourceId())) {
                            this.usersList.remove(packet.getSourceId(), this.packet.getAddress());
                        }
                    }
                }

                System.out.println("RECEIVED REG -- STORING");
                if (!this.packetMap.containsKey(packet.getDestinationId())) {
                    this.packetMap.put(packet.getDestinationId(), new ArrayList<>());
                }
                this.packetMap.get(packet.getDestinationId()).add(packet);
                System.out.println("STORED");
            }
        }
    }

}
