package com.notayessir.common.packet;

/**
 *  计算数据帧 payload 的长度
 */
public interface Computable {

    /**
     * 计算数据帧 payload 的长度
     * @return  数据帧 payload 的长度
     */
    int compute();


}
