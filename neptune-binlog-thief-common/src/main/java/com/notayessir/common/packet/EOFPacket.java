package com.notayessir.common.packet;


public class EOFPacket extends Packet {


    private final int header;
    private final int warnings;
    private final int statusFlags;


    public EOFPacket(PacketHeader packetHeader, int header, int warnings, int statusFlag) {
        this.packetHeader = packetHeader;
        this.header = header;
        this.warnings = warnings;
        this.statusFlags = statusFlag;
    }

    public int getHeader() {
        return header;
    }

    public int getWarnings() {
        return warnings;
    }

    public int getStatusFlags() {
        return statusFlags;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.GENERIC_EOF_PACKET;
    }
}
