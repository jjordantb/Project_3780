package com;

/**
 * Created by Jordan on 11/20/2016.
 */
public class Message {

    public static final int PAYLOAD_LENGTH = 1002;

    private final int seqNum;
    private final Type type;
    private final String sourceId;
    private final String destinationId;
    private String payload;

    public Message(final int seqNum, final Type type, final String sourceId, final String destinationId, final String payload) {
        this.type = type;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.payload = payload;
        this.seqNum = seqNum;
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

//    public List<Packet> toPackets() {
//        int cnt = 0;
//        final List<Packet> packets = new ArrayList<>();
//        final byte[] payloadBytes = this.payload != null ? this.payload.getBytes() : "null".getBytes();
//        byte[] bytes;
//
//        int size = payloadBytes.length / PAYLOAD_LENGTH;
//        for (int i = 0; i < size + 1; i++) {
//            bytes = new byte[PAYLOAD_LENGTH];
//            if (i != size) {
//                for (int j = 0; j < PAYLOAD_LENGTH; j++) {
//                    bytes[j] = payloadBytes[(PAYLOAD_LENGTH * i) + j];
//                }
//            } else {
//                for (int j = 0; j < payloadBytes.length - (i * PAYLOAD_LENGTH); j++) {
//                    bytes[j] = payloadBytes[(PAYLOAD_LENGTH * i) + j];
//                }
//            }
//            packets.add(new Packet(cnt, this.type, this.sourceId, this.destinationId, new String(bytes)));
//            cnt++;
//        }
//        return packets;
//    }

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

    public int getSeqNum() {
        return seqNum;
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
