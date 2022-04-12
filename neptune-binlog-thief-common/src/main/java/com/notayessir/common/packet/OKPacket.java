package com.notayessir.common.packet;


public class OKPacket extends Packet {

    private final int header;
    private final int affectedRows;
    private final int lastInsertId;
    private final int statusFlags;
    private final int warnings;

//    private final String info;
//    private final String sessionStateChanges;



    public OKPacket(PacketHeader packetHeader, int header, int affectedRows, int lastInsertId, int statusFlags, int okWarnings) {
        this.packetHeader = packetHeader;
        this.header = header;
        this.affectedRows = affectedRows;
        this.lastInsertId = lastInsertId;
        this.statusFlags = statusFlags;
        this.warnings = okWarnings;
    }

    public int getHeader() {
        return header;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public int getLastInsertId() {
        return lastInsertId;
    }

    public int getStatusFlags() {
        return statusFlags;
    }

    public int getWarnings() {
        return warnings;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.GENERIC_OK_PACKET;
    }
}
