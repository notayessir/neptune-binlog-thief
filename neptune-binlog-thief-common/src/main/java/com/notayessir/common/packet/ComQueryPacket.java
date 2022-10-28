package com.notayessir.common.packet;

/**
 * 通用的 sql 数据帧，例如 select 等 sql 语句
 */
public class ComQueryPacket extends Packet implements Computable {


    private final byte commandId;

    private final String queryText;

    public ComQueryPacket(PacketHeader packetHeader, String queryText) {
        this.packetHeader = packetHeader;
        this.commandId = 3;
        this.queryText = queryText;
        this.packetHeader.setPayloadLength(compute());
    }

    @Override
    public int compute() {
        return 1 + queryText.length();
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.COM_QUERY;
    }

    public byte getCommandId() {
        return commandId;
    }

    public String getQueryText() {
        return queryText;
    }
}
