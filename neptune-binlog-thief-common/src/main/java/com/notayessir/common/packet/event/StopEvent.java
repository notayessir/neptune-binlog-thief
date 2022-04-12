package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class StopEvent extends Packet {


    public StopEvent(PacketHeader packetHeader, EventHeader eventHeader) {
        this.packetHeader = packetHeader;
        this.eventHeader = eventHeader;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.STOP_EVENT;
    }
}
