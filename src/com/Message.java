package com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordan on 11/20/2016.
 */
public class Message {

    private final Type type;
    private final int sequenceNumber;
    private final String sourceId;
    private final String destinationId;
    private final String payload;

    public Message(final int sequenceNumber, final Type type,
                   final String sourceId, final String destinationId, final String payload) {
        this.type = type;
        this.sequenceNumber = sequenceNumber;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.payload = payload;

    }

    /*
        Byte indexes
        [0] = Sequence Number
        [1] = Type
        [2-11] SourceID
        [12-21] DestinationID
        [22-(length - 1 (max(1002))] Payload
     */
    public List<byte[]> encode() {
        final List<byte[]> bytes = new ArrayList<>();
        final byte[] payload = this.payload.getBytes();
        if (payload.length > 1002) {
            // split up into multiple messages
        } else {
            final byte[] message = new byte[22 + payload.length];
            message[0] = 0;
            message[1] = this.type.getValue();
            final byte[] source = this.sourceId.getBytes();
            for (int i = 2; i <= 11; i++) {
                message[i] = source[i - 2];
            }
            final byte[] destination = this.destinationId.getBytes();
            for (int i = 12; i <= 21; i++) {
                message[i] = destination[i - 12];
            }
            for (int i = 22; i < 22 + payload.length; i++) {
                message[i] = payload[i -22];
            }
            bytes.add(message);
        }
        return bytes;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(this.sequenceNumber).append(", ");
        switch (this.type.getValue()){
            case 0:
                stringBuilder.append("SEND, ");
                break;
            case 1:
                stringBuilder.append("GET, ");
                break;
            case 2:
                stringBuilder.append("ACK, ");
                break;
        }
        stringBuilder.append(this.sourceId).append(", ").append(this.destinationId).append(", ")
                .append(this.payload).append("]");
        return stringBuilder.toString();
    }

    public Type getType() {
        return type;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
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
