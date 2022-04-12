package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class FormatDescEvent extends Packet {

    private final int binlogVersion;

    private final String mysqlServerVersion;

    private final int createTimestamp;

    private final int eventHeaderLen;

    private final byte [] eventTypeHeaderLen;

    public FormatDescEvent(PacketHeader packetHeader, EventHeader eventHeader, int binlogVersion, String mysqlServerVersion, int createTimestamp, int eventHeaderLen, byte [] eventTypeHeaderLen) {
        this.packetHeader = packetHeader;
        this.eventHeader = eventHeader;
        this.binlogVersion = binlogVersion;
        this.mysqlServerVersion = mysqlServerVersion;
        this.createTimestamp = createTimestamp;
        this.eventHeaderLen = eventHeaderLen;
        this.eventTypeHeaderLen = eventTypeHeaderLen;
    }


    public int getBinlogVersion() {
        return binlogVersion;
    }

    public String getMysqlServerVersion() {
        return mysqlServerVersion;
    }

    public int getCreateTimestamp() {
        return createTimestamp;
    }

    public int getEventHeaderLen() {
        return eventHeaderLen;
    }

    public byte[] getEventTypeHeaderLen() {
        return eventTypeHeaderLen;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.FORMAT_DESCRIPTION_EVENT;
    }
}
