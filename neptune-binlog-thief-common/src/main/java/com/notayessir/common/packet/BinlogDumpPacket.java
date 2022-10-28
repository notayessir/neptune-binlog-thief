package com.notayessir.common.packet;


import com.notayessir.common.util.ByteUtil;

/**
 * 请求主节点发送 binlog 事件
 */
public class BinlogDumpPacket extends Packet implements Computable {

    private byte binlogDump;

    private byte [] binlogPos;

    private byte [] flags;

    private byte [] serverId;

    private String binlogFilename;


    public BinlogDumpPacket(PacketHeader packetHeader, byte[] binlogPos, int serverId, String binlogFilename) {
        this.packetHeader = packetHeader;
        this.binlogDump = 0x12;
        this.binlogPos = binlogPos;
        this.flags = new byte[]{0, 0};
        this.serverId = ByteUtil.intToBytes(serverId);
        this.binlogFilename = binlogFilename;
        this.packetHeader.setPayloadLength(compute());
    }

    @Override
    public int compute() {
        return 1 + binlogPos.length + flags.length + serverId.length + binlogFilename.length();
    }

    public byte getBinlogDump() {
        return binlogDump;
    }

    public void setBinlogDump(byte binlogDump) {
        this.binlogDump = binlogDump;
    }

    public byte[] getBinlogPos() {
        return binlogPos;
    }

    public void setBinlogPos(byte[] binlogPos) {
        this.binlogPos = binlogPos;
    }

    public byte[] getFlags() {
        return flags;
    }

    public void setFlags(byte[] flags) {
        this.flags = flags;
    }

    public byte[] getServerId() {
        return serverId;
    }

    public void setServerId(byte[] serverId) {
        this.serverId = serverId;
    }

    public String getBinlogFilename() {
        return binlogFilename;
    }

    public void setBinlogFilename(String binlogFilename) {
        this.binlogFilename = binlogFilename;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.BINLOG_DUMP_PACKET;
    }
}
