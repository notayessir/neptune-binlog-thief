package com.notayessir.common.packet;


public abstract class Packet {

    protected PacketHeader packetHeader;

    protected EventHeader eventHeader;

    public EventHeader getEventHeader() {
        return eventHeader;
    }

    public void setEventHeader(EventHeader eventHeader) {
        this.eventHeader = eventHeader;
    }

    public PacketHeader getPacketHeader() {
        return packetHeader;
    }

    public void setPacketHeader(PacketHeader packetHeader) {
        this.packetHeader = packetHeader;
    }

    public abstract PacketType getPacketType();
}
