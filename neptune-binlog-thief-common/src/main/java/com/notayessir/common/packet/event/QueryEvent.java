package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class QueryEvent extends Packet {


    private final int slaveProxyId;

    private final int executionTime;

//    private final int schemaLength;

    private final int errorCode;

//    private final int statusVarsLength;

    private final byte [] statusVars;

    private final String schema;

    /**
     * 00
     */
//    private byte skip;

    private final String query;

    public QueryEvent(PacketHeader packetHeader, EventHeader eventHeader, int slaveProxyId, int executionTime,
                      int errorCode, byte [] statusVars, String schema, String query) {
        this.eventHeader = eventHeader;
        this.packetHeader = packetHeader;
        this.slaveProxyId = slaveProxyId;
        this.executionTime = executionTime;
        this.errorCode = errorCode;
        this.statusVars = statusVars;
        this.schema = schema;
        this.query = query;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.QUERY_EVENT;
    }


    public int getSlaveProxyId() {
        return slaveProxyId;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public byte[] getStatusVars() {
        return statusVars;
    }

    public String getSchema() {
        return schema;
    }

    public String getQuery() {
        return query;
    }
}
