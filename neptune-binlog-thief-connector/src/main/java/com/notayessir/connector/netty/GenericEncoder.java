package com.notayessir.connector.netty;

import com.notayessir.connector.encoder.PacketEncoder;
import com.notayessir.connector.encoder.impl.AbsPacketEncoder;
import com.notayessir.common.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * 将发送的帧编码成字节流
 */
public class GenericEncoder extends MessageToByteEncoder<Object> {


    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        Packet packet = (Packet) msg;
        PacketEncoder<Object> packetEncoder = AbsPacketEncoder.getPacketEncoder(packet.getPacketType());
        packetEncoder.encode(msg, out);
    }



}
