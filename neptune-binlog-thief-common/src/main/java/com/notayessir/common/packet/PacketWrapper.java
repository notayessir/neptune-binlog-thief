package com.notayessir.common.packet;

public class PacketWrapper<P> {

    private P packet;

    private boolean completed;


    public PacketWrapper() {
    }

    public PacketWrapper(P packet) {
        this.packet = packet;
        this.completed = true;
    }

    public P getPacket() {
        return packet;
    }

    public void setPacket(P packet) {
        this.packet = packet;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void completedPacket(P packet){
        this.completed = true;
        this.packet = packet;
    }
}
