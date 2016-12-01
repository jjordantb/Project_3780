package com;

/**
 * Created by Jordan on 11/20/2016.
 */
public class Messages {

    /*
        Does it need error catching for malformed messages?
     */
    public static Message decodeToPacket(final byte[] packet) {
        int sequenceNumber = packet[0];
        int type = packet[1];
        byte[] source = new byte[10];
        byte[] destination = new byte[10];
        byte[] payload = new byte[packet.length - 22];
        for (int i = 2; i <= 11; i++) {
            source[i - 2] = packet[i];
        }
        for (int i = 12; i <= 21; i++) {
            destination[i - 12] = packet[i];
        }
        for (int i = 22; i < 22 + payload.length; i++) {
            payload[i -22] = packet[i];
        }
        return new Message(sequenceNumber,
                type == 0 ? Message.Type.SEND : type == 1 ? Message.Type.GET : Message.Type.ACK,
                new String(source), new String(destination), new String(payload));
    }

//    /*
//        Sorts the Packet array so that the sequence numbers are in ascending order then reconstructs the message
//     */
//    public static Message decodeToMessage(final Packet[] packets) {
//        Arrays.sort(packets);
//        final Message message = new Message(packets[0].getType(),
//                packets[0].getSourceId(), packets[0].getDestinationId(),
//                packets[0].getPayload());
//        for (int i = 1; i < packets.length; i++) {
//            message.appendPayload(packets[i].getPayload());
//        }
//        return message;
//    }

}
