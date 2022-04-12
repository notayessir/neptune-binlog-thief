package com.notayessir.connector;

/**
 * binlog slave 抽象接口
 */
public interface BinlogThief {

    /**
     * 启动 slave，连接 master 读取 binlog 事件
     */
    void start();


    /**
     * 停止 slave，释放/关闭相关资源
     */
    void stop() ;

}
