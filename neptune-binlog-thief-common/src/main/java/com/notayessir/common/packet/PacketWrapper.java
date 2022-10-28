package com.notayessir.common.packet;

/**
 * 数据帧包装类，仅仅是封装数据帧，并带一些判断方法
 * @param <P>   数据帧类型
 */
public class PacketWrapper<P> {

    /**
     * 数据帧
     */
    private P packet;

    /**
     * 字节长度是否完整能组成数据帧
     */
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
