package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

/**
 * https://dev.mysql.com/doc/dev/mysql-server/latest/control__events_8h_source.html
 */
public class AnonymousGTIDEvent extends Packet {


    private final int GTIDFlags;

    private final byte [] sid;

    private final byte [] gno;

    private final int logicTS;

    private final long lastCommitted;

    private final long sequenceNumber;

    /**
     * mysql doc says, if immediateCommitTimestamp == originCommitTimestamp, then return immediateCommitTimestamp
     */
    private final long immediateCommitTimestamp;
//    private long originCommitTimestamp;

    private final long transactionLength;

    /**
     * mysql doc says, if immediateServerVersion == originServerVersion, then return immediateServerVersion
     */
    private final int immediateServerVersion;
//    private int originServerVersion;

    private final byte [] other;


    public AnonymousGTIDEvent(PacketHeader packetHeader, EventHeader eventHeader,
                              int GTIDFlags, byte[] sid, byte[] gno, int logicTS,
                              long lastCommitted, long sequenceNumber, long immediateCommitTimestamp,
                              long transactionLength, int immediateServerVersion, byte[] other) {
        this.packetHeader = packetHeader;
        this.eventHeader = eventHeader;
        this.GTIDFlags = GTIDFlags;
        this.sid = sid;
        this.gno = gno;
        this.logicTS = logicTS;
        this.lastCommitted = lastCommitted;
        this.sequenceNumber = sequenceNumber;
        this.immediateCommitTimestamp = immediateCommitTimestamp;
        this.transactionLength = transactionLength;
        this.immediateServerVersion = immediateServerVersion;
        this.other = other;
    }


    public int getGTIDFlags() {
        return GTIDFlags;
    }

    public byte[] getSid() {
        return sid;
    }

    public byte[] getGno() {
        return gno;
    }

    public int getLogicTS() {
        return logicTS;
    }

    public long getLastCommitted() {
        return lastCommitted;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public long getImmediateCommitTimestamp() {
        return immediateCommitTimestamp;
    }

    public long getTransactionLength() {
        return transactionLength;
    }

    public int getImmediateServerVersion() {
        return immediateServerVersion;
    }

    public byte[] getOther() {
        return other;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.ANONYMOUS_GTID_EVENT;
    }
}
