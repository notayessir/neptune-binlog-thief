package com.notayessir.common.packet;

public class BinlogPacket {


    private Object packet;

    public BinlogPacket(Object packet) {
        this.packet = packet;
    }


    public Object getPacket() {
        return packet;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }
}
