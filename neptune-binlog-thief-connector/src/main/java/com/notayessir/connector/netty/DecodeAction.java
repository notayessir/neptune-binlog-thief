package com.notayessir.connector.netty;

/**
 * 连接阶段，用于处理各类型帧的逻辑
 */
public enum DecodeAction {

    /**
     * 常驻
     */
    NONE(-1),

    /**
     * 已经发送关闭 checksum 命令
     */
    CLOSE_CHECKSUM(1),

    /**
     * 已经发送注册 slave 帧
     */
    REGISTER_SLAVE(2),

    /**
     * 已经发送 dump 帧
     */
    BINLOG_DUMP(3),

    ;


    private final int order;

    DecodeAction(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
