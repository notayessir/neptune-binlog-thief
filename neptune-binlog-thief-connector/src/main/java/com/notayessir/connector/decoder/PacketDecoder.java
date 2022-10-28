package com.notayessir.connector.decoder;


import io.netty.buffer.ByteBuf;

/**
 * 数据帧解码器，将字节流中的字节读取出来组成数据帧
 * @param <P>   数据帧类型
 */
public interface PacketDecoder<P> {

    /**
     * 字节流解码
     * @param in    字节流
     * @return      数据帧
     */
    P decode(ByteBuf in);

}
