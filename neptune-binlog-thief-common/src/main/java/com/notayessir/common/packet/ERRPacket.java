package com.notayessir.common.packet;


public class ERRPacket extends Packet {

    private final int header;

    private final int errorCode;

    private final String sqlStateMarker;

    private final String sqlState;

    private final String errorMessage;

    public ERRPacket(PacketHeader packetHeader, int header, int errorCode, String sqlStateMarker, String sqlState, String errorMessage) {
        this.packetHeader = packetHeader;
        this.header = header;
        this.errorCode = errorCode;
        this.sqlStateMarker = sqlStateMarker;
        this.sqlState = sqlState;
        this.errorMessage = errorMessage;
    }

    public int getHeader() {
        return header;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getSqlStateMarker() {
        return sqlStateMarker;
    }

    public String getSqlState() {
        return sqlState;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.GENERIC_ERR_PACKET;
    }
}
