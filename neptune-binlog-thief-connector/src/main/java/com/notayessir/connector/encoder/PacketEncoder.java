package com.notayessir.connector.encoder;

import io.netty.buffer.ByteBuf;


/**
 * 数据帧编码器，将输出的数据帧对象编码成字节流
 * @param <P>   数据帧
 */
public abstract class PacketEncoder<P> {


    /**
     * 将数据帧编码为字节流
     * @param packet        数据帧
     * @param out           字节流
     */
    public abstract void encode(P packet, ByteBuf out);

}
