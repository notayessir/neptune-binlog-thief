package com.notayessir.common.packet;

/**
 * 数据帧的数据组成
 */
public abstract class Packet {

    /**
     * 数据帧头部
     */
    protected PacketHeader packetHeader;

    /**
     * 事件帧头部
     */
    protected EventHeader eventHeader;

    public EventHeader getEventHeader() {
        return eventHeader;
    }

    public void setEventHeader(EventHeader eventHeader) {
        this.eventHeader = eventHeader;
    }

    public PacketHeader getPacketHeader() {
        return packetHeader;
    }

    public void setPacketHeader(PacketHeader packetHeader) {
        this.packetHeader = packetHeader;
    }

    /**
     * 获取事件帧的类型
     * @return  事件帧的类型
     */
    public abstract PacketType getPacketType();
}
