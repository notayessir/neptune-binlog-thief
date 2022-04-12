package com.notayessir.common.packet;

public class EventHeader {


    protected long timestamp;

    protected byte eventType;

    protected int serverId;

    protected int eventSize;

    protected int logPos;

    protected int flags;

    public EventHeader(long timestamp, byte eventType, int serverId, int eventSize, int logPos, int flags) {
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.serverId = serverId;
        this.eventSize = eventSize;
        this.logPos = logPos;
        this.flags = flags;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte getEventType() {
        return eventType;
    }

    public void setEventType(byte eventType) {
        this.eventType = eventType;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getEventSize() {
        return eventSize;
    }

    public void setEventSize(int eventSize) {
        this.eventSize = eventSize;
    }

    public int getLogPos() {
        return logPos;
    }

    public void setLogPos(int logPos) {
        this.logPos = logPos;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}
