package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class PreviousGTIDSEvent extends Packet {

    private final byte[] other;

    public PreviousGTIDSEvent(PacketHeader packetHeader, EventHeader eventHeader, byte[] other) {
        this.packetHeader = packetHeader;
        this.eventHeader = eventHeader;
        this.other = other;
    }

    public byte[] getOther() {
        return other;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.PREVIOUS_GTIDS_EVENT;
    }
}
