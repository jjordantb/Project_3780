package client;

import com.LogHandler;
import com.Message;
import com.Messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Jordan on 11/26/2016.
 */
public class Receiver implements Runnable {

    private final DatagramSocket socket;
    private final Thread thread;

    private boolean running = true;

    public Receiver(final DatagramSocket socket) {
        this.socket = socket;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() { // needs to handle ACK's
        while (this.running) {
            byte[] recData = new byte[Message.PAYLOAD_LENGTH + 22];
            DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
            try {
                socket.receive(recPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LogHandler.log(Messages.decodeToPacket(recData).toString());
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
