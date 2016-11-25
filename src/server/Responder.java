package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Jordan on 11/24/2016.
 */
public class Responder implements Runnable {

    private final DatagramSocket socket;
    private final DatagramPacket packet;

    private final Thread thread;

    public Responder(final DatagramSocket socket, final DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;

        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        final byte[] bytes = this.packet.getData();

    }

}
