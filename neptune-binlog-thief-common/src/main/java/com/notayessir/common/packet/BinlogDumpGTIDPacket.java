package com.notayessir.common.packet;


public class BinlogDumpGTIDPacket extends Packet implements Computable {

    private byte binlogDumpGTID;

    private byte [] flags;

    private byte [] serverId;

    private byte [] binlogFilenameLen;

    private String binlogFilename;

    private byte [] binlogPos;

    private byte [] dataSize;

    private String data;


    public byte getBinlogDumpGTID() {
        return binlogDumpGTID;
    }

    public void setBinlogDumpGTID(byte binlogDumpGTID) {
        this.binlogDumpGTID = binlogDumpGTID;
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

    public byte[] getBinlogFilenameLen() {
        return binlogFilenameLen;
    }

    public void setBinlogFilenameLen(byte[] binlogFilenameLen) {
        this.binlogFilenameLen = binlogFilenameLen;
    }

    public String getBinlogFilename() {
        return binlogFilename;
    }

    public void setBinlogFilename(String binlogFilename) {
        this.binlogFilename = binlogFilename;
    }

    public byte[] getBinlogPos() {
        return binlogPos;
    }

    public void setBinlogPos(byte[] binlogPos) {
        this.binlogPos = binlogPos;
    }

    public byte[] getDataSize() {
        return dataSize;
    }

    public void setDataSize(byte[] dataSize) {
        this.dataSize = dataSize;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int compute() {
        return 0;
    }


    @Override
    public PacketType getPacketType() {
        return PacketType.BINLOG_DUMP_GTID_PACKET;
    }
}
