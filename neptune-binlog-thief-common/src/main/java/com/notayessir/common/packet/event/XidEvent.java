package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class XidEvent extends Packet {


    private final long xid;

    public XidEvent(PacketHeader packetHeader, EventHeader eventHeader, long xid) {
        this.eventHeader = eventHeader;
        this.packetHeader = packetHeader;
        this.xid = xid;
    }

    public long getXid() {
        return xid;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.XID_EVENT;
    }
}
