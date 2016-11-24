package client;

import com.Message;
import com.Messages;
import com.Packet;

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

        final Message message = new Message(Message.Type.SEND, "0000000001", "0000000002",
                "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "1111111112111111111211111111121111111112111111111211111111121111111112111111111211111111121111111112" +
                        "123oh;jdwj;awdoihawdpoawd");
        System.out.println(message.toPackets().size());

        for (Packet packet : message.toPackets()) {
            System.out.println(packet.toString());
        }

        List<Packet> packets = message.toPackets();
        System.out.println(Messages.decodeToMessage(packets.toArray(new Packet[packets.size()])));

//        try {
//            DatagramSocket socket = new DatagramSocket();
//            InetAddress address = InetAddress.getByName("localhost");
//            while (true) {
//                final List<byte[]> bytes = new Message(0, Message.Type.SEND, "0000000000", "0000000001", "This is the payload you slut muffin").encode();
//                final DatagramPacket packet = new DatagramPacket(bytes.get(0), bytes.get(0).length, address, 6969);
//                socket.send(packet);
//                Thread.sleep(1000);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
