package com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordan on 11/20/2016.
 */
public class Message {

    public static final int PAYLOAD_LENGTH = 1002;

    private final Type type;
    private final String sourceId;
    private final String destinationId;
    private String payload;

    public Message(final Type type, final String sourceId, final String destinationId, final String payload) {
        this.type = type;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.payload = payload;

    }


    public List<Packet> toPackets() {
        int cnt = 0;
        final List<Packet> packets = new ArrayList<>();
        final byte[] payloadBytes = this.payload != null ? this.payload.getBytes() : "null".getBytes(); // legit never seen "".getBytes() before lol
        byte[] bytes;

        int size = payloadBytes.length / PAYLOAD_LENGTH;
        for (int i = 0; i < size + 1; i++) {
            bytes = new byte[PAYLOAD_LENGTH];
            if (i != size) {
                for (int j = 0; j < PAYLOAD_LENGTH; j++) {
                    bytes[j] = payloadBytes[(PAYLOAD_LENGTH * i) + j];
                }
            } else {
                for (int j = 0; j < payloadBytes.length - (i * PAYLOAD_LENGTH); j++) {
                    bytes[j] = payloadBytes[(PAYLOAD_LENGTH * i) + j];
                }
            }
            packets.add(new Packet(cnt, this.type, this.sourceId, this.destinationId, new String(bytes)));
            cnt++;
        }
        return packets;
    }

    @Override
    public String toString() {
        return "[" + this.type.getValue() + ", " + this.sourceId + ", " + this.destinationId + ", " + this.payload + "]";
    }

    public Type getType() {
        return type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getPayload() {
        return payload;
    }

    public void appendPayload(final String str) {
        this.payload = this.payload + str;
    }

    public enum Type {

        SEND((byte) 0x0),
        GET((byte) 0x1),
        ACK((byte) 0x2);

        private final byte value;

        Type(final byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }

    }
}
