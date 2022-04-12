package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class RowQueryEvent extends Packet {


    private int length;

    private String query;

    public RowQueryEvent(PacketHeader packetHeader, EventHeader eventHeader, int length, String query) {
        this.packetHeader = packetHeader;
        this.eventHeader = eventHeader;
        this.length = length;
        this.query = query;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.ROWS_QUERY_EVENT;
    }


}
