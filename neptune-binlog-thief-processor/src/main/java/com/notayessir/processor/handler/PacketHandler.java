package com.notayessir.processor.handler;


import com.notayessir.processor.configure.AppConfiguration;

/**
 * binlog 事件处理器
 * @param <P>
 */
public interface PacketHandler<P> {

    /**
     * 处理 binlog 事件
     * @param packet    binlog 事件
     */
    void handle(P packet);

    /**
     * 是否跳过处理
     * @param packet                binlog 事件
     * @param appConfiguration      app 配置
     * @return                      是否跳过
     */
    boolean skipHandle(P packet, AppConfiguration appConfiguration);

    /**
     * 处理之前的逻辑
     * @param packet    binlog 事件
     */
    void beforeHandle(P packet);
}
