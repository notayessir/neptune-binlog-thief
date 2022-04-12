package com.notayessir.common.packet;

/**
 * MySQL base packets
 * @see <a href="https://dev.mysql.com/doc/internals/en/mysql-packet.html">MySQL Packets</a>
 */
public class PacketHeader {

    /**
     * 3 byte
     */
    protected int payloadLength;

    /**
     * 1 byte
     */
    protected int sequenceId;

    public PacketHeader(int payloadLength, int sequenceId) {
        this.payloadLength = payloadLength;
        this.sequenceId = sequenceId;
    }

    public PacketHeader(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public PacketHeader() {
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(int payloadLength) {
        this.payloadLength = payloadLength;
    }


    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
}
