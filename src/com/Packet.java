package com;

/**
 * Created by Jordan Florchinger on 11/21/2016.
 */
public class Packet implements Comparable<Packet> {


    private final int sequenceNumber;
    private final Message.Type type;
    private final String sourceId;
    private final String destinationId;
    private final String payload;

    public Packet(final int sequenceNumber, final Message.Type type,
                  final String sourceId, final String destinationId, final String payload) {
        this.sequenceNumber = sequenceNumber;
        this.type = type;
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
    public byte[] encode() {
        final byte[] payload = this.payload != null ? this.payload.getBytes() : "null".getBytes();
        if (payload.length > Message.PAYLOAD_LENGTH) {
            return null;
        } else {
            final byte[] message = new byte[22 + payload.length];
            message[0] = 0;
            message[1] = this.type.getValue();
            final byte[] source = this.sourceId != null ? this.sourceId.getBytes() : "0000000000".getBytes();
            for (int i = 2; i <= 11; i++) {
                message[i] = source[i - 2];
            }
            final byte[] destination = this.destinationId != null ? this.destinationId.getBytes() : "0000000000".getBytes();
            for (int i = 12; i <= 21; i++) {
                message[i] = destination[i - 12];
            }
            for (int i = 22; i < 22 + payload.length; i++) {
                message[i] = payload[i -22];
            }
            return message;
        }
    }


    @Override
    public int compareTo(Packet packet) {
        return this.sequenceNumber - packet.getSequenceNumber();
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

    public String getPayload() {
        return payload;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public Message.Type getType() {
        return type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

}
