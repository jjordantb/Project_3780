package com;

/**
 * Created by Jordan on 11/20/2016.
 */
public class Messages {

    /*
        Does it need error catching for malformed messages?
     */
    public static Message decode(final byte[] message) {
        int sequenceNumber = message[0];
        int type = message[1];
        byte[] source = new byte[10];
        byte[] destination = new byte[10];
        byte[] payload = new byte[message.length - 22];
        for (int i = 2; i <= 11; i++) {
            source[i - 2] = message[i];
        }
        for (int i = 12; i <= 21; i++) {
            destination[i - 12] = message[i];
        }
        for (int i = 22; i < 22 + payload.length; i++) {
            payload[i -22] = message[i];
        }
        return new Message(sequenceNumber,
                type == 0 ? Message.Type.SEND : type == 1 ? Message.Type.GET : Message.Type.ACK,
                new String(source), new String(destination), new String(payload));
    }

}
