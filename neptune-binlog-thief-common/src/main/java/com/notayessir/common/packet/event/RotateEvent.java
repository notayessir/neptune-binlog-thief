package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class RotateEvent extends Packet {

    private final long position;

    private final String nextBinlogName;

    public RotateEvent(PacketHeader packetHeader, EventHeader eventHeader, long position, String nextBinlogName) {
        this.packetHeader = packetHeader;
        this.eventHeader = eventHeader;
        this.position = position;
        this.nextBinlogName = nextBinlogName;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.ROTATE_EVENT;
    }

    public long getPosition() {
        return position;
    }

    public String getNextBinlogName() {
        return nextBinlogName;
    }

}
