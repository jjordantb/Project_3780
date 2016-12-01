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
