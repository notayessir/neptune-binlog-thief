package com.notayessir.connector.netty;


import com.notayessir.common.packet.PacketType;

/**
 * 解码切换器
 */
public class DecodeSwitch {


    /**
     * 下一个帧的类型
     */
    private PacketType nextPacketType;

    /**
     * 下一个逻辑动作
     */
    private DecodeAction nextAction;

    public DecodeSwitch(PacketType nextPacketType) {
        this.nextPacketType = nextPacketType;
    }

    public DecodeAction getNextAction() {
        return nextAction;
    }

    public void setNextAction(DecodeAction action) {
        this.nextAction = action;
    }

    public PacketType getNextPacketType() {
        return nextPacketType;
    }

    public void setNextPacketType(PacketType nextPacketType) {
        this.nextPacketType = nextPacketType;
    }
}
