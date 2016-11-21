package client;

import com.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by Jordan on 11/14/2016.
 */
public class ClientApplication {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");

            while (true) {
                final List<byte[]> bytes = new Message(0, Message.Type.SEND, "0000000000", "0000000001", "This is the payload you slut muffin").encode();
                final DatagramPacket packet = new DatagramPacket(bytes.get(0), bytes.get(0).length, address, 6969);
                socket.send(packet);
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
